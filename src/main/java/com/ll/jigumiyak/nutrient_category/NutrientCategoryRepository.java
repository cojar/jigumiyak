package com.ll.jigumiyak.nutrient_category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutrientCategoryRepository extends JpaRepository<NutrientCategory, Long> {
    Optional<NutrientCategory> findByCategoryName(String categoryName);

    @Query("SELECT nc FROM NutrientCategory nc JOIN nc.surveyList s WHERE s.id = :surveyId")
    Optional<NutrientCategory> findBySurveyId(@Param("surveyId") Long surveyId);
}
