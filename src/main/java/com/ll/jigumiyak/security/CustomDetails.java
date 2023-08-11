package com.ll.jigumiyak.security;

import com.ll.jigumiyak.user.SiteUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomDetails implements UserDetails, OAuth2User {

    private final SiteUser user;
    private final Map<String, Object> attributes;

    public CustomDetails(SiteUser user) {
        this(user, null);
    }

    public CustomDetails(SiteUser user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public String getUsername() {
        return this.user.getLoginId();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : user.getAuthorityList()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
