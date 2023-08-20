package com.ll.jigumiyak.nutrient_category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrientCategoryRepository extends JpaRepository<NutrientCategory, Long> {
}
