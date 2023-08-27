package com.ll.jigumiyak.purchase_item;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.purchase.Purchase;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PurchaseItem extends BaseEntity {

    private Integer count;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Purchase purchase;
}

