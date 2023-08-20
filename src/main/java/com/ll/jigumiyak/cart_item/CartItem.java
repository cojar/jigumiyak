package com.ll.jigumiyak.cart_item;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.cart.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartItem extends BaseEntity {

    @ManyToOne
    private Cart cart;
}
