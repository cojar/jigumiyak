package com.ll.jigumiyak.cart_item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndProductId(Long id, Long productId);
}
