package com.ll.jigumiyak.cart_item;

import com.ll.jigumiyak.cart.Cart;
import com.ll.jigumiyak.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductAndCart(Product product, Cart cart);
}
