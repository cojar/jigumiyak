package com.ll.jigumiyak.survey;

import com.ll.jigumiyak.nutrient.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    @Query("SELECT n FROM Survey s JOIN s.nutrientCategory c JOIN c.nutrientList n WHERE s.id = :surveyId")
    List<Nutrient> findNutrientListBySurveyId(@Param("surveyId") Long surveyId);

    List<Survey> findByNutrientCategoryId(Long nutrientCategoryId);
}
