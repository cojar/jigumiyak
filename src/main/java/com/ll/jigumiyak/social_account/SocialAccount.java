package com.ll.jigumiyak.social_account;

import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String providerId;

    private String email;

    private String name;

    @ManyToOne
    private SiteUser parent;
}
