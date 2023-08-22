package com.ll.jigumiyak.cart;

import com.ll.jigumiyak.cart_item.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByPurchaserId(Long id);

    @Query("SELECT ci FROM Cart c JOIN c.itemList ci WHERE c.purchaser.id = :purchaserId")
    List<CartItem> findItemListByPurchaserId(@Param("purchaserId") Long purchaserId);
}
