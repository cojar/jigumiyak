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

    private Integer price;

    private Integer quantity;
    private Long inventory;

    private Long hit;

    @OneToOne
    private GenFile thumbnailImg;

    @OneToOne
    private GenFile detailImg;

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

    public Double averageRating() {
        if (reviewList == null || reviewList.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        int reviewCount = 0;

        for (ProductReview review : reviewList) {
            totalRating += review.getStar_rating();
            reviewCount++;
        }

        double average = (double) totalRating / reviewCount;
        return Math.round(average * 10.0) / 10.0; // 소수점 한 자리까지 반올림
    }
}
