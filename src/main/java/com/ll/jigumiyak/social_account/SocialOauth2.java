package com.ll.jigumiyak.social_account;

import java.util.Map;

public interface SocialOauth2 {

    String getCodeRedirectUri();

    String getAccessToken(String code, String state);

    Map<String, String> getAttributes(String accessToken);
}
