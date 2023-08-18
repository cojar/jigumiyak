package com.ll.jigumiyak.product_review;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.user.SiteUser;
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
public class ProductReview extends BaseEntity {

    @ManyToOne
    private Product product;

    @ManyToOne
    private SiteUser reviewer;
}
//  주문 / 결제
//
