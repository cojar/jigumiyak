package com.ll.jigumiyak.social_account;

import com.ll.jigumiyak.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findByProviderId(String providerId);

    Optional<SocialAccount> findByProviderAndParent(String provider, SiteUser parent);
}
