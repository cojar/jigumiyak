package com.ll.jigumiyak.cart_item;

import com.ll.jigumiyak.cart.Cart;
import com.ll.jigumiyak.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    public CartItem createCartItem(Cart cart, Product product, int count) {
        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .count(count)
                .build();
        cartItemRepository.save(cartItem);
        return cartItem;
    }
}
