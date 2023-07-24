package com.ll.jigumiyak.product;

import com.ll.jigumiyak.product_category.ProductCategory;
import com.ll.jigumiyak.product_review.ProductReview;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private ProductCategory productCategory;
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductReview> reviewList;
}
