package com.ll.jigumiyak.security;

import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomSecurityService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Optional<SiteUser> _user = this.userRepository.findByLoginId(loginId);
        if (_user.isEmpty()) {
            throw new BadCredentialsException("가입된 통합회원 계정이 아닙니다.");
        }

        SiteUser user = _user.get();

        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String authority : user.getAuthorityList()) {
            authorityList.add(new SimpleGrantedAuthority(authority));
        }

        return new User(user.getLoginId(), user.getPassword(), authorityList);
    }

}
