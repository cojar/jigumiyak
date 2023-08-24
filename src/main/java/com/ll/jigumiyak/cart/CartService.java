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

//    public Long addCart(CartItemForm cartItemForm, SiteUser owner, Product product){
//
//        CartItem savedCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
//
//        if(savedCartItem != null){
//            addCount(savedCartItem, Integer.valueOf(cartItemForm.getCount()));
//            return savedCartItem.getId();
//        } else {
//            CartItem cartItem = cartItemService.createCartItem(cart, product, cartItemForm.getCount());
//            return cartItem.getId();
//        }
//    }
//
//    private void addCount(CartItem cartItem, Integer count){
//        cartItem.toBuilder()
//                .count(cartItem.getCount() + count)
//                .build();
//        cartItemRepository.save(cartItem);
//    }
}
