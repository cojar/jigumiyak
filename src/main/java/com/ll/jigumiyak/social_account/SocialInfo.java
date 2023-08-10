package com.ll.jigumiyak.social_account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class SocialInfo {

    private Map<String, String> attributes;

    public SocialInfo(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        attributes = new HashMap<>();

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("kakao")) {

            attributes.put("providerId", provider + "_" + oAuth2User.getAttributes().get("id").toString());
            attributes.put("email", ((Map) oAuth2User.getAttributes().get("kakao_account")).get("email").toString());
            attributes.put("name", ((Map) oAuth2User.getAttributes().get("properties")).get("nickname").toString());

        } else if (provider.equals("google")) {

            attributes.put("providerId", provider + "_" + oAuth2User.getAttributes().get("sub").toString());
            attributes.put("email", oAuth2User.getAttributes().get("email").toString());
            attributes.put("name", oAuth2User.getAttributes().get("name").toString());

        }

        log.info(provider.toUpperCase() + " login requested");
        log.info("providerId: " + attributes.get("providerId"));
        log.info("email: " + attributes.get("email"));
        log.info("name: " + attributes.get("name"));
    }
}
