package com.ll.jigumiyak.cart;

import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;

    private Cart createCart(SiteUser owner) {

        Cart cart = Cart.builder()
                .owner(owner)
                .build();

        this.cartRepository.save(cart);

        return cart;
    }

    public Cart getCartByOwner(SiteUser owner) {

        Cart cart = this.cartRepository.findByOwner(owner).orElse(null);

        if (cart == null) {
            cart = this.createCart(owner);
        }

        return cart;
    }


}
