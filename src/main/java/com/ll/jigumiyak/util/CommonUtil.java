package com.ll.jigumiyak.util;

import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommonUtil {

    private final UserService userService;

    public SiteUser usernameToUser(String loginId) {
        return this.userService.getUserByLoginId(loginId);
    }
}
