package com.ll.jigumiyak.social_account;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SocialAccount extends BaseEntity {

    private String provider;

    @Column(unique = true)
    private String providerId;

    private String email;

    private String name;

    @ManyToOne
    private SiteUser parent;
}
