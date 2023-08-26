package com.ll.jigumiyak.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByLoginId(String loginId);
    Optional<SiteUser> findByEmail(String email);
    Optional<SiteUser> findByLoginIdAndEmail(String loginId, String email);
    String findEmailByLoginId(String loginId);
}
