package com.ll.jigumiyak.nutrient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
    Optional<Nutrient> findByName(String name);
}
