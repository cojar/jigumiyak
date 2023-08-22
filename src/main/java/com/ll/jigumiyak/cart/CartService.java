package com.ll.jigumiyak.cart;

import com.ll.jigumiyak.cart_item.CartItem;
import com.ll.jigumiyak.cart_item.CartItemForm;
import com.ll.jigumiyak.cart_item.CartItemRepository;
import com.ll.jigumiyak.cart_item.CartItemService;
import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemService cartItemService;

    public Long addCart(CartItemForm cartItemForm, SiteUser user, Product product){
        Cart cart = cartRepository.findByPurchaserId(user.getId());
        if(cart == null){
            createCart(user);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if(savedCartItem != null){
            addCount(savedCartItem, Integer.valueOf(cartItemForm.getCount()));
            return savedCartItem.getId();
        } else {
            CartItem cartItem = cartItemService.createCartItem(cart, product, cartItemForm.getCount());
            return cartItem.getId();
        }
    }

    private void createCart(SiteUser user){
        Cart cart = Cart.builder()
                .purchaser(user)
                .build();
        cartRepository.save(cart);
    }
    private void addCount(CartItem cartItem, Integer count){
        cartItem.toBuilder()
                .count(cartItem.getCount() + count)
                .build();
        cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartList(Long purchaserId) {
        return cartRepository.findItemListByPurchaserId(purchaserId);
    }
}
