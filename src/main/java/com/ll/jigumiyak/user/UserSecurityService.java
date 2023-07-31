package com.ll.jigumiyak.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Optional<SiteUser> _user = this.userRepository.findByLoginId(loginId);
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        SiteUser user = _user.get();

        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String authority : user.getAuthorityList()) {
            authorityList.add(new SimpleGrantedAuthority(authority));
        }

        return new User(user.getLoginId(), user.getPassword(), authorityList);
    }

}
