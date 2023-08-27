package com.ll.jigumiyak.user;

import com.ll.jigumiyak.address.Address;
import com.ll.jigumiyak.address.AddressService;
import com.ll.jigumiyak.security.CustomRole;
import com.ll.jigumiyak.util.RsData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    @GetMapping("/signup")
    public String signup(UserSignupForm userSignupForm) {
        return "signup";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity signup(@Valid UserSignupForm userSignupForm, BindingResult bindingResult) {

        log.info("loginId: " + userSignupForm.getLoginId());
        log.info("password: " + userSignupForm.getPassword());
        log.info("passwordConfirm: " + userSignupForm.getPasswordConfirm());
        log.info("email: " + userSignupForm.getEmail());
        log.info("inputCode: " + userSignupForm.getInputCode());
        log.info("genCode: " + userSignupForm.getGenCode());
        log.info("address.zoneCode: " + userSignupForm.getAddress().getZoneCode());
        log.info("address.mainAddress: " + userSignupForm.getAddress().getMainAddress());
        log.info("address.subAddress: " + userSignupForm.getAddress().getSubAddress());

        if (bindingResult.hasErrors()) {
            List<String> errorList = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.info("field: " + fieldError.getField());
                log.info("code: " + fieldError.getCode());
                log.info("message: " + fieldError.getDefaultMessage());
                errorList.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
        }

        if (this.userService.isDuplicatedLoginId(userSignupForm.getLoginId())) {
            bindingResult.rejectValue("loginId", "Duplicated", "입력한 아이디가 이미 존재합니다.");
        }

        if (!userSignupForm.getPassword().equals(userSignupForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordNotMatch", "입력한 비밀번호가 일치하지 않습니다.");
        }

        if (this.userService.isDuplicatedEmail(userSignupForm.getEmail())) {
            bindingResult.rejectValue("email", "Duplicated", "입력한 이메일이 이미 존재합니다.");
        }

        if (!this.userService.isMatched(userSignupForm.getEmail() + userSignupForm.getInputCode(), userSignupForm.getGenCode())) {
            bindingResult.rejectValue("inputCode", "CodeNotMatch", "입력한 이메일 인증번호가 일치하지 않습니다.");
        }

        if (userSignupForm.getAddress().getZoneCode() == null || userSignupForm.getAddress().getMainAddress().equals("")) {
            bindingResult.rejectValue("address", "NotEmpty", "주소는 필수 항목입니다.");
        }

        if (bindingResult.hasErrors()) {
            List<String> errorList = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.info("field: " + fieldError.getField());
                log.info("code: " + fieldError.getCode());
                log.info("message: " + fieldError.getDefaultMessage());
                errorList.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
        }

        Address address = this.addressService.create(userSignupForm.getAddress().getZoneCode(),
                userSignupForm.getAddress().getMainAddress(),
                userSignupForm.getAddress().getSubAddress());

        SiteUser user = this.userService.create(userSignupForm.getLoginId(),
                userSignupForm.getPassword(),
                userSignupForm.getEmail(),
                address);

        return ResponseEntity.status(HttpStatus.OK)
                .body(String.format("%s님 환영합니다.\n확인 버튼을 누르시면 로그인 페이지로 이동합니다.", user.getLoginId()));
    }

    @GetMapping("/signup/loginId")
    @ResponseBody
    public ResponseEntity checkLoginId(@RequestParam("loginId") String loginId) {

        log.info("loginId: " + loginId);

        if (loginId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "아이디를 입력해주세요", ""));
        }

        if (!loginId.matches("(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{8,20}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-2", "영문과 숫자를 조합해서 8자 이상 20자 이하로 입력해주세요", ""));
        }

        if (this.userService.isDuplicatedLoginId(loginId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-3", "이미 사용중인 아이디입니다", ""));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "사용 가능한 아이디입니다", ""));
    }

    @GetMapping("/signup/email")
    @ResponseBody
    public ResponseEntity genEmailCode(@RequestParam("email") String email) {

        log.info("email: " + email);

        if (email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "이메일을 입력해주세요", ""));
        }

        if (!email.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-2", "올바른 이메일 형식이 아닙니다", ""));
        }

        if (this.userService.isDuplicatedEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-3", "이미 사용중인 이메일입니다", ""));
        }

        String[] codeBits = this.userService.genSecurityCode(email, 8);

        if (!this.userService.sendEmail(email, codeBits[0], "이메일 인증코드", "인증코드를")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-4", "이메일 발송 중에 오류가 발생했습니다", ""));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "이메일 인증번호 발송이 완료되었습니다", codeBits[1]));
    }

    @GetMapping("/signup/code")
    @ResponseBody
    public ResponseEntity checkEmailCode(@RequestParam("email") String email,
                                         @RequestParam("inputCode") String inputCode,
                                         @RequestParam("genCode") String genCode) {

        log.info("email: " + email);
        log.info("inputCode: " + inputCode);
        log.info("genCode: " + genCode);

        if (inputCode.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "이메일 인증번호를 입력해주세요", ""));
        }

        if (!this.userService.isMatched(email + inputCode, genCode)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-2", "이메일 인증번호가 일치하지 않습니다", ""));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "이메일 인증이 완료되었습니다", ""));
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request,
                        @RequestParam(value = "error", defaultValue = "") String error,
                        @RequestParam(value = "field", defaultValue = "") String field) {

        String refererUri = request.getHeader("Referer");
        log.info("referer: " + refererUri);

        if (refererUri != null && !refererUri.contains("/user/login")
                && !refererUri.contains("/user/signup") && !refererUri.contains("/user/find")) {
            request.getSession().setAttribute("refererUri", refererUri);
        }

        Map<String, String> errors = new HashMap<>();
        errors.put(field, error);
        model.addAttribute("errors", errors);

        return "login";
    }

    @GetMapping("/find")
    public String findUser() {
        return "user_find";
    }

    @GetMapping("/find/loginId")
    @ResponseBody
    public ResponseEntity findLoginId(@RequestParam("email") String email) {

        log.info("email: " + email);

        if (email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "이메일을 입력해주세요", ""));
        }

        if (!email.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-2", "올바른 이메일 형식이 아닙니다", ""));
        }

        SiteUser user = this.userService.getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-3", "입력한 이메일로 가입된 통합회원 계정이 없습니다", ""));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", String.format("가입된 통합회원 아이디는 \"%s\" 입니다.", user.getLoginId()), user.getLoginId()));
    }

    @PostMapping("/find/password")
    @ResponseBody
    public ResponseEntity findPassword(@RequestParam("loginId") String loginId,
                                       @RequestParam("email") String email) {

        log.info("loginId: " + loginId);
        log.info("email: " + email);

        Map<String, RsData> errors = new HashMap<>();

        // 입력 필드 에러

        if (loginId.isEmpty()) {
            errors.put("loginId", new RsData<>("F-1", "아이디를 입력해주세요", ""));
        }

        if (email.isEmpty()) {
            errors.put("email", new RsData<>("F-2", "이메일을 입력해주세요", ""));
        }

        if (!email.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,}$") && !errors.containsKey("email")) {
            errors.put("email", new RsData<>("F-3", "올바른 이메일 형식이 아닙니다", ""));
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        SiteUser user = this.userService.getUserByLoginIdAndEmail(loginId, email);

        // 계정 조회 에러

        if (user == null) {

            if (this.userService.getUserByLoginId(loginId) == null) {
                errors.put("loginId", new RsData<>("F-4", "가입된 통합회원 계정이 아닙니다", ""));

            } else if (this.userService.getUserByEmail(email) == null) {
                errors.put("email", new RsData<>("F-5", "이메일을 확인해주세요", ""));

            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // 조회 성공 후 로직

        String[] codeBits = this.userService.genSecurityCode("", 12);

        if (!this.userService.sendEmail(email, codeBits[0], "임시비밀번호", "임시비밀번호를")) {
            errors.put("loginId", new RsData<>("F-6", "이메일 발송 중에 오류가 발생했습니다", ""));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        this.userService.modifyPassword(user, codeBits[0], true);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "입력한 이메일로 임시비밀번호를 발송하였습니다", ""));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String my(Model model, Principal principal) {

        SiteUser user = this.userService.getUserByLoginId(principal.getName());

        model.addAttribute("user", user);

        return "mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String modify(Model model, Principal principal,
                         UserModifyPasswordForm userModifyPasswordForm) {

        SiteUser user = this.userService.getUserByLoginId(principal.getName());

        model.addAttribute("user", user);

        return "user_modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/password")
    @ResponseBody
    public ResponseEntity modifyPassword(@Valid UserModifyPasswordForm userModifyPasswordForm, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "바인딩 에러", ""));
        }

        SiteUser user = this.userService.getUserByLoginId(principal.getName());

        if (!this.userService.isMatched(userModifyPasswordForm.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-2", "기존 비밀번호 매칭 에러", ""));
        }

        if (!userModifyPasswordForm.getNewPassword().equals(userModifyPasswordForm.getNewPasswordConfirm())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-3", "신규 비밀번호 매칭 에러", ""));
        }

        this.userService.modifyPassword(user, userModifyPasswordForm.getNewPassword(), false);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "비밀번호 변경이 완료되었습니다.\n로그인을 다시 진행해주세요.", "/user/logout"));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/oldPassword")
    @ResponseBody
    public ResponseEntity checkOldPassword(@RequestParam("oldPassword") String oldPassword, Principal principal) {

        log.info("oldPassword", oldPassword);

        SiteUser user = this.userService.getUserByLoginId(principal.getName());

        if (!this.userService.isMatched(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "비밀번호가 일치하지 않습니다", ""));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "비밀번호가 일치합니다", ""));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/address")
    @ResponseBody
    public ResponseEntity modifyAddress(@RequestParam(value = "zoneCode", defaultValue = "-1") Integer zoneCode,
                                        @RequestParam(value = "mainAddress", defaultValue = "") String mainAddress,
                                        @RequestParam("subAddress") String subAddress,
                                        Principal principal) {

        if (zoneCode == -1 || mainAddress.equals("")) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new RsData<>("S-1", "저장을 완료했습니다", "/user/mypage"));
        }

        SiteUser user = this.userService.getUserByLoginId(principal.getName());

        Address address = user.getAddress();

        if (address != null) {
            this.addressService.modify(address, zoneCode, mainAddress, subAddress);
        } else {
            address = this.addressService.create(zoneCode, mainAddress, subAddress);
            this.userService.saveAddress(user, address);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "저장을 완료했습니다", "/user/mypage"));
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/authority")
    public ResponseEntity authority(@RequestParam("id") Long id, Principal principal) {

        SiteUser user = this.userService.getUser(id);
        SiteUser admin = this.userService.getUserByLoginId(principal.getName());

        // checked 권한
        Map<String, String> checkedAttributes = new HashMap<>();
        for (GrantedAuthority authority : user.getAuthorities()) {
            checkedAttributes.put(authority.getAuthority(), "authority_" + authority.getAuthority());
        }

        // disabled 권한
        Map<String, String> disabledAttributes = new HashMap<>();
        disabledAttributes.put("super_admin", "authority_super_admin");
        disabledAttributes.put("admin", "authority_admin");
        disabledAttributes.put("seller", "authority_seller");
        disabledAttributes.put("user", "user");

        if (admin.getAuthoritiesInline().contains("총관리자")) {
            if (!user.getAuthoritiesInline().contains("총관리자")) {
                disabledAttributes.remove("admin");
                disabledAttributes.remove("seller");
                disabledAttributes.remove("user");
            }
        } else {
            if (!user.getAuthoritiesInline().contains("총관리자") && !user.getAuthoritiesInline().contains("관리자")) {
                disabledAttributes.remove("seller");
                disabledAttributes.remove("user");
            }
        }

        Map<String, Map<String, String>> attributes = new HashMap<>();
        attributes.put("checked", checkedAttributes);
        attributes.put("disabled", disabledAttributes);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "요청한 회원의 권한 상태를 반환합니다", attributes));
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/authority")
    public ResponseEntity authority(@RequestParam("loginId") String loginId,
                                    @RequestParam List<String> authority) {

        SiteUser user = this.userService.getUserByLoginId(loginId);

        log.info("authority: {}", authority);

        Integer authorityDec = 0;
        for (String type : authority) {
            authorityDec += CustomRole.getDecCodeByType(type);
        }

        this.userService.modifyAuthorities(user, authorityDec);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("id", user.getId().toString());
        attributes.put("authoritiesInline", user.getAuthoritiesInline());

        log.info("modified authorities: " + user.getAuthoritiesInline());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "요청한 회원의 권한 상태를 변경했습니다", attributes));
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/withdraw")
    public ResponseEntity withdraw(@RequestParam("id") Long id, Principal principal) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "해당 회원 탈퇴 처리를 완료했습니다", ""));
    }
}
