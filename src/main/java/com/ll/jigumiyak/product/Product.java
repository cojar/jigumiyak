package com.ll.jigumiyak.product;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.file.GenFile;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.product_category.ProductCategory;
import com.ll.jigumiyak.product_review.ProductReview;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@ToString
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product extends BaseEntity {

    @Column(length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int price;

    private Integer quantity;

    private Long hit;

    @OneToOne
    private GenFile thumbnailImg;

    @ManyToOne
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductReview> reviewList;

    @ManyToMany
    Set<SiteUser> voter;

    @ManyToMany
    @JoinTable(name = "product_nutrient_mapping",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "nutrient_id"))
    private List<Nutrient> nutrientList;
}
