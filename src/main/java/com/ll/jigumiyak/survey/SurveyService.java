package com.ll.jigumiyak.survey;

import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final NutrientCategoryRepository nutrientCategoryRepository;

    public void saveSurveyWithCategory(String question, NutrientCategory nutrientCategory) {
        Survey survey = Survey.builder()
                .question(question)
                .nutrientCategory(nutrientCategory)
                        .build();

        this.surveyRepository.save(survey);
    }
}
