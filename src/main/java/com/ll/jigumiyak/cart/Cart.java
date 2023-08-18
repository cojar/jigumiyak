package com.ll.jigumiyak.cart;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.cart_item.CartItem;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart extends BaseEntity {

    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
    private List<CartItem> itemList;

    @ManyToOne
    private SiteUser purchaser;
}
