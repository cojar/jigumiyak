package com.ll.jigumiyak.nutrient_category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutrientCategoryRepository extends JpaRepository<NutrientCategory, Long> {
    Optional<NutrientCategory> findByCategoryName(String categoryName);
}
