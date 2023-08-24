package com.ll.jigumiyak.cart_item;

import com.ll.jigumiyak.cart.Cart;
import com.ll.jigumiyak.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    private CartItem create(Product product, Cart cart) {

        CartItem cartItem = CartItem.builder()
                .count(0)
                .product(product)
                .cart(cart)
                .build();

        this.cartItemRepository.save(cartItem);

        return cartItem;
    }

    public CartItem getCartItemByProductAndCart(Product product, Cart cart) {

        CartItem cartItem = this.cartItemRepository.findByProductAndCart(product, cart)
                .orElse(null);

        if (cartItem == null) {

            cartItem = this.create(product, cart);
        }

        return cartItem;
    }

    public void updateCount(Integer count, CartItem cartItem) {

        cartItem = cartItem.toBuilder()
                .count(cartItem.getCount() + count)
                .build();

        this.cartItemRepository.save(cartItem);
    }
}
