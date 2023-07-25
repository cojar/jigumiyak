package com.ll.jigumiyak.user;

import com.ll.jigumiyak.social_account.SocialAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<SocialAccount> socialAccountList;
}
