package com.ll.jigumiyak.purchase_item;

import com.ll.jigumiyak.cart_item.CartItem;
import com.ll.jigumiyak.purchase.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;

    public PurchaseItem create(CartItem cartItem, Purchase purchase) {

        PurchaseItem purchaseItem = PurchaseItem.builder()
                .count(cartItem.getCount())
                .product(cartItem.getProduct())
                .purchase(purchase)
                .build();

        this.purchaseItemRepository.save(purchaseItem);

        return purchaseItem;
    }
}
