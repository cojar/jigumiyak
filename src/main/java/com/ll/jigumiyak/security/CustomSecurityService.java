package com.ll.jigumiyak.security;

import com.ll.jigumiyak.social_account.SocialAccount;
import com.ll.jigumiyak.social_account.SocialAccountRepository;
import com.ll.jigumiyak.social_account.SocialInfo;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomSecurityService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SocialAccountRepository socialAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        SiteUser user = this.userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadCredentialsException("가입된 통합회원 계정이 아닙니다"));

        return new CustomDetails(user);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, String> attributes = new SocialInfo(userRequest, oAuth2User).getAttributes();

        SocialAccount socialAccount = this.socialAccountRepository.findByProviderId(attributes.get("providerId"))
                .orElse(null);

        if (socialAccount == null) {

            SiteUser user = this.userRepository.findByEmail(attributes.get("email"))
                    .orElse(null);

            if (user == null) {

                user = SiteUser.builder().build();
                this.userRepository.save(user);

                user = user.toBuilder()
                        .authority(CustomRole.USER.getDecCode())
                        .loginId(attributes.get("provider") + "_" + user.getId())
                        .password("")
                        .email(attributes.get("email"))
                        .build();

                this.userRepository.save(user);
            }

            socialAccount = SocialAccount.builder()
                    .provider(attributes.get("provider"))
                    .providerId(attributes.get("providerId"))
                    .email(attributes.get("email"))
                    .name(attributes.get("name"))
                    .parent(user)
                    .build();

            this.socialAccountRepository.save(socialAccount);
        }

        return new CustomDetails(socialAccount.getParent());
    }
}
