package com.ll.jigumiyak.user;

import com.ll.jigumiyak.address.Address;
import com.ll.jigumiyak.security.CustomRole;
import com.ll.jigumiyak.social_account.SocialAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer authority;

    @Column(unique = true)
    private String loginId;

    private String password;

    @Column(unique = true)
    private String email;

    @OneToOne
    private Address address;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<SocialAccount> socialAccountList;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    private LocalDateTime lastLoginDate;

    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        int binLen = CustomRole.values().length;
        String authority = Integer.toBinaryString(this.getAuthority());
        authority = "0".repeat(binLen - authority.length()) + authority;

        for (int i = 0; i < authority.length(); i++) {
            if (authority.charAt(authority.length() - i - 1) == '1') {
                authorities.add(new SimpleGrantedAuthority(CustomRole.getTypeByCode(i)));
            }
        }

        return authorities;
    }
}
