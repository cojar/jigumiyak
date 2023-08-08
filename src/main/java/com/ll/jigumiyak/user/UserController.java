package com.ll.jigumiyak.user;

import com.ll.jigumiyak.address.Address;
import com.ll.jigumiyak.address.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String my() {
        return "mypage";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request,
                        @RequestParam(value = "error", defaultValue = "") String error,
                        @RequestParam(value = "field", defaultValue = "") String field) {

        String refererUri = request.getHeader("Referer");
        log.info(request.toString());
        log.info(refererUri);

        if (refererUri != null && !refererUri.contains("/user/login") && !refererUri.contains("/user/signup")) {
            request.getSession().setAttribute("refererUri", refererUri);
        }

        Map<String, String> errors = new HashMap<>();
        errors.put(field, error);
        model.addAttribute("errors", errors);

        return "login";
    }

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
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.info(fieldError.toString());
                log.info("field: " + fieldError.getField());
                log.info("code: " + fieldError.getCode());
                log.info("message: " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldErrors());
        }

        if (this.userService.isDuplicatedId(userSignupForm.getLoginId())) {
            bindingResult.rejectValue("loginId", "Duplicated", "입력한 아이디가 이미 존재합니다.");
        }

        if (!userSignupForm.getPassword().equals(userSignupForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordNotMatch", "입력한 비밀번호가 일치하지 않습니다.");
        }

        if (this.userService.isDuplicatedEmail(userSignupForm.getEmail())) {
            bindingResult.rejectValue("email", "Duplicated", "입력한 이메일이 이미 존재합니다.");
        }

        if (!this.userService.isMatched(userSignupForm.getInputCode(), userSignupForm.getGenCode())) {
            bindingResult.rejectValue("inputCode", "CodeNotMatch", "입력한 이메일 인증번호가 일치하지 않습니다.");
        }

        if (userSignupForm.getAddress().getZoneCode() == null || userSignupForm.getAddress().getMainAddress().equals("")) {
            bindingResult.rejectValue("address", "NotEmpty", "주소는 필수 항목입니다.");
        }

        if (bindingResult.hasErrors()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getFieldErrors());

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

    @GetMapping("/signup/emailCode")
    @ResponseBody
    public String genEmailCode(@RequestParam("email") String email) {

        String[] codeBits = this.userService.genSecurityCode(8);

        this.userService.sendEmail(email, codeBits[0], "이메일 인증코드", "인증코드를");

        return codeBits[1];
    }

    @PostMapping("/signup/emailCode")
    @ResponseBody
    public String confirmEmailCode(@RequestParam("inputCode") String inputCode, @RequestParam("genCode") String genCode) {

        if(this.userService.isMatched(inputCode, genCode)) {
            return "success";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다.");
        }
    }
}
