package com.ll.jigumiyak.social_account;

import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocialAccountService {

    private final SocialAccountRepository socialAccountRepository;
    private final KakaoOauth2 kakaoOauth2;
    private final GoogleOauth2 googleOauth2;

    public SocialAccount create(SiteUser parent, Map<String, String> attributes) {

        SocialAccount socialAccount = SocialAccount.builder()
                .provider(attributes.get("provider"))
                .providerId(attributes.get("providerId"))
                .email(attributes.get("email"))
                .name(attributes.get("name"))
                .parent(parent)
                .build();

        this.socialAccountRepository.save(socialAccount);

        return socialAccount;
    }

    public SocialAccount getSocialAccountByProviderId(String providerId) {
        return this.socialAccountRepository.findByProviderId(providerId)
                .orElse(null);
    }

    public SocialAccount getSocialAccountByProviderAndParent(String provider, SiteUser parent) {
        return this.socialAccountRepository.findByProviderAndParent(provider, parent)
                .orElse(null);
    }

    public void delete(SocialAccount socialAccount) {
        this.socialAccountRepository.delete(socialAccount);
    }

    public String getCodeRedirectUri(String provider) {

        if (provider.equals("kakao")) {
            return this.kakaoOauth2.getCodeRedirectUri();
        } else if (provider.equals("google")) {
            return this.googleOauth2.getCodeRedirectUri();
        } else {
            return null;
        }
    }

    public String getAccessToken(String provider, String code, String state) {

        if (provider.equals("kakao")) {
            return this.kakaoOauth2.getAccessToken(code, state);
        } else if (provider.equals("google")) {
            return this.googleOauth2.getAccessToken(code, state);
        } else {
            return null;
        }
    }

    public Map<String, String> getAttributes(String provider, String accessToken) {

        if (provider.equals("kakao")) {
            return this.kakaoOauth2.getAttributes(accessToken);
        } else if (provider.equals("google")) {
            return this.googleOauth2.getAttributes(accessToken);
        } else {
            return null;
        }
    }
}
