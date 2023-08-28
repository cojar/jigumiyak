package com.ll.jigumiyak.nutrient_answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NutrientAnswerRepository extends JpaRepository<NutrientAnswer, Long> {

    List<NutrientAnswer> findByAnswerId(Long surveyAnswerId);
}
