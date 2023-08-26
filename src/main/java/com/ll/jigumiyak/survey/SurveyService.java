package com.ll.jigumiyak.survey;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final NutrientCategoryRepository nutrientCategoryRepository;

    public Survey createSurvey(String question, NutrientCategory nutrientCategory) {
        Survey survey = Survey.builder()
                .question(question)
                .nutrientCategory(nutrientCategory)
                .build();
        surveyRepository.save(survey);
        return survey;
    }

    public Survey getSurvey(long id) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(id);
        if(optionalSurvey.isPresent()){
            return optionalSurvey.get();
        } else {
            throw new DataNotFoundException("not found Survey");
        }
    }
}
