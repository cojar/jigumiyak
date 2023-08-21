package com.ll.jigumiyak.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Product> findAllByKeyword(@Param("keyword") String keyword,
                                   Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.nutrientList n JOIN n.categoryList c WHERE c.categoryName = :categoryName")
    Page<Product> findByNutrientCategoryName(@Param("categoryName") String categoryName,
                                             Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.nutrientList n JOIN n.categoryList c WHERE c.categoryName = :categoryName " +
            "AND (p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Product> findByCategoryNameAndKeyword(@Param("keyword") String keyword,
                                               @Param("categoryName") String categoryName,
                                               Pageable pageable);
}
