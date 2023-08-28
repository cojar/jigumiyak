package com.ll.jigumiyak.product_review;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.file.GenFile;
import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column
    private int star_rating;
    @OneToOne(optional = false)
    private GenFile reviewImg;

    @ManyToOne
    private Product product;

    @ManyToOne
    private SiteUser reviewer;
}
