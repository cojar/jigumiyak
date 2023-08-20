package com.ll.jigumiyak.nutrient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
    Optional<Nutrient> findByName(String name);

    boolean existsByName(String name);


    @Query("SELECT n.name FROM Nutrient n WHERE LOWER(n.name) LIKE %:searchWord%")
    List<String> findNutrientByNutrientName(@Param("searchWord") String searchWord);
}
