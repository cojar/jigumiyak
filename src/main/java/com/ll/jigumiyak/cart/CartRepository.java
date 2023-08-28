package com.ll.jigumiyak.cart;

import com.ll.jigumiyak.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByOwner(SiteUser owner);
}
