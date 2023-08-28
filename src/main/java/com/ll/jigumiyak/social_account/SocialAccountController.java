package com.ll.jigumiyak.social_account;

import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import com.ll.jigumiyak.util.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Controller
public class SocialAccountController {

    private final SocialAccountService socialAccountService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/link")
    @ResponseBody
    public ResponseEntity link(@RequestParam("provider") String provider) {

        String redirectUri = this.socialAccountService.getCodeRedirectUri(provider);

        log.info(provider + " redirectUri: " + redirectUri);

        if (redirectUri == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "허용되는 소셜 타입이 아닙니다", ""));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "코드 URI 를 반환했습니다", redirectUri));
    }

    @PreAuthorize("isAuthenticated")
    @GetMapping("/code/{provider}")
    public String createSocialAccount(@PathVariable("provider") String provider,
                                      @RequestParam(value = "code") String code, @RequestParam(value = "state", defaultValue = "") String state,
                                      RedirectAttributes redirectAttributes, Principal principal) {

        log.info(provider + " code: " + code);
        log.info(provider + " state: " + state);

        String accessToken = this.socialAccountService.getAccessToken(provider, code, state);

        log.info(provider + " accessToken: " + accessToken);

        if (accessToken == null) {
            log.info("허용되는 소셜 타입이 아닙니다");
            redirectAttributes.addFlashAttribute("isLinkFail", "허용되는 소셜 타입이 아닙니다");
            return "redirect:/user/modify";
        }

        Map<String, String> attributes = this.socialAccountService.getAttributes(provider, accessToken);

        log.info(provider + " attributes: " + attributes);

        if (attributes == null) {
            log.info("허용되는 소셜 타입이 아닙니다");
            redirectAttributes.addFlashAttribute("isLinkFail", "허용되는 소셜 타입이 아닙니다");
            return "redirect:/user/modify";
        }

        SiteUser parent = this.userService.getUserByLoginId(principal.getName());

        if (this.socialAccountService.getSocialAccountByProviderId(attributes.get("providerId")) != null) {

            log.info("이미 연결된 소셜 계정입니다");
            redirectAttributes.addFlashAttribute("isLinkFail", "이미 연결된 소셜 계정입니다");

        } else if (this.userService.isDuplicatedEmail(attributes.get("email"))) {

            if (attributes.get("email").equals(parent.getEmail())) {

                log.info("해당 소셜 계정 연결을 완료했습니다");
                redirectAttributes.addFlashAttribute("isLinkSuccess", "해당 소셜 계정 연결을 완료했습니다");
                this.socialAccountService.create(parent, attributes);
            } else {

                log.info("이미 사용중인 이메일입니다");
                redirectAttributes.addFlashAttribute("isLinkFail", "이미 사용중인 이메일입니다");
            }

        } else {

            log.info("해당 소셜 계정 연결을 완료했습니다");
            redirectAttributes.addFlashAttribute("isLinkSuccess", "해당 소셜 계정 연결을 완료했습니다");
            this.socialAccountService.create(parent, attributes);
        }

        return "redirect:/user/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/unlink")
    @ResponseBody
    public ResponseEntity unlink(@RequestParam("provider") String provider, Principal principal) {

        log.info("provider: " + provider);

        SiteUser parent = this.userService.getUserByLoginId(principal.getName());
        SocialAccount socialAccount = this.socialAccountService.getSocialAccountByProviderAndParent(provider, parent);

        if (socialAccount == null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new RsData<>("F-1", "해당 소셜 계정이 없습니다", ""));
        }

        this.socialAccountService.delete(socialAccount);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "해당 소셜 계정 연결을 해제했습니다", ""));
    }
}
