package com.ll.jigumiyak.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@Component
public class FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {


        log.info(exception.toString());

        String error;
        String field;

        if (exception instanceof BadCredentialsException) {
            error = "비밀번호가 일치하지 않습니다";
            field = "password";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            error = exception.getMessage();
            field = "loginId";
        } else {
            error = "알 수 없는 이유로 로그인에 실패하였습니다";
            field = "unknown";
        }

        error = URLEncoder.encode(error, "UTF-8"); // 한글 인코딩 깨진 문제 방지
        response.sendRedirect(String.format("/user/login?error=%s&field=%s", error, field));
    }
}
