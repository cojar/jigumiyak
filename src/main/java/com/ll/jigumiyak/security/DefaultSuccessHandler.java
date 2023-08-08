package com.ll.jigumiyak.security;

import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // last login date update
        Optional<SiteUser> _user = this.userRepository.findByLoginId(authentication.getName());
        if (_user.isPresent()) {
            SiteUser user = _user.get();
            user.setLastLoginDate(LocalDateTime.now());
            this.userRepository.save(user);
        }

        // refererUri check
        String refererUri = (String) request.getSession().getAttribute("refererUri");
        log.info(refererUri);
        request.getSession().removeAttribute("refererUri");

        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        String redirectUri = savedRequest != null ? savedRequest.getRedirectUrl() : null;
        log.info(redirectUri);

        if (redirectUri != null) {
            response.sendRedirect(redirectUri);
        } else if (refererUri != null) {
            response.sendRedirect(refererUri);
        } else {
            response.sendRedirect("/");
        }
    }
}
