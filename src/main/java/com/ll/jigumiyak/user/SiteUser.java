package com.ll.jigumiyak.user;

import com.ll.jigumiyak.address.Address;
import com.ll.jigumiyak.social_account.SocialAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<String> authorityList;

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

    private LocalDateTime lastLoginDate;
}
