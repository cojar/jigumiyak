package com.ll.jigumiyak.security;

import com.ll.jigumiyak.social_account.SocialAccount;
import com.ll.jigumiyak.social_account.SocialAccountRepository;
import com.ll.jigumiyak.social_account.SocialInfo;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                .orElseThrow(() -> new InternalAuthenticationServiceException("통합회원 가입 또는 로그인 후 소셜 계정 연결을 진행해주세요"));

        return new CustomDetails(socialAccount.getParent());
    }
}
