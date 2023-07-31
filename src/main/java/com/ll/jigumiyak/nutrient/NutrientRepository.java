package com.ll.jigumiyak.nutrient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
}
