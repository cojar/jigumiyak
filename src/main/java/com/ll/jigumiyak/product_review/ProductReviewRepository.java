package com.ll.jigumiyak.product_review;

import com.ll.jigumiyak.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    Page<ProductReview> findAllByProduct(Product product, Pageable pageable);

    @Query("SELECT ROUND(AVG(r.star_rating), 1) FROM ProductReview r WHERE r.product.id = :productId")
    Double getAverageStarRatingForProduct(@Param("productId") Long productId);
}
