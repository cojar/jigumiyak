package com.ll.jigumiyak.nutrient_caution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrientCautionRepository extends JpaRepository<NutrientCaution, Long> {
}
