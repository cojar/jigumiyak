package com.ll.jigumiyak.survey;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientService;
import com.ll.jigumiyak.nutrient_answer.NutrientAnswer;
import com.ll.jigumiyak.nutrient_answer.NutrientAnswerService;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryService;
import com.ll.jigumiyak.survey_answer.SurveyAnswer;
import com.ll.jigumiyak.survey_answer.SurveyAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final NutrientAnswerService nutrientAnswerService;
    private final NutrientCategoryRepository nutrientCategoryRepository;
    private final NutrientCategoryService nutrientCategoryService;

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
        if (optionalSurvey.isPresent()) {
            return optionalSurvey.get();
        } else {
            throw new DataNotFoundException("not found Survey");
        }
    }

    public List<Nutrient> getNutrientListBySurvey(Long surveyId) {
        return surveyRepository.findNutrientListBySurveyId(surveyId);
    }

    public List<Survey> getSurveyListByCategory(Long surveyCategoryId) {
        return surveyRepository.findByNutrientCategoryId(surveyCategoryId);
    }

    public Page<Survey> getSurveyList(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return surveyRepository.findAll(pageable);
    }

    public Map<Nutrient, Double> getAverageScoresByNutrient(Map<Long, Long> answerIdMap) {
        Map<Nutrient, List<Double>> nutrientScoresMap = groupScoresByNutrient(answerIdMap);
        return calculateAverageScores(nutrientScoresMap);
    }


    private Map<Nutrient, List<Double>> groupScoresByNutrient(Map<Long, Long> answerIdMap) {
        Map<Nutrient, List<Double>> nutrientScoresMap = new HashMap<>();

        for (Map.Entry<Long, Long> entry : answerIdMap.entrySet()) {
            Long surveyId = entry.getKey();
            Long surveyAnswerId = entry.getValue();

            Survey survey = surveyRepository.findById(surveyId).orElse(null);
            if (survey != null) {
                List<NutrientAnswer> nutrientAnswers = nutrientAnswerService.getAnswerListByAnswer(surveyAnswerId);

                for (NutrientAnswer nutrientAnswer : nutrientAnswers) {
                    Nutrient nutrient = nutrientAnswer.getNutrient();
                    double score = nutrientAnswer.getScore();

                    nutrientScoresMap.computeIfAbsent(nutrient, k -> new ArrayList<>()).add(score);
                }
            }
        }

        return nutrientScoresMap;
    }

    private Map<Nutrient, Double> calculateAverageScores(Map<Nutrient, List<Double>> nutrientScoresMap) {
        Map<Nutrient, Double> averageScores = new HashMap<>();

        for (Map.Entry<Nutrient, List<Double>> entry : nutrientScoresMap.entrySet()) {
            Nutrient nutrient = entry.getKey();
            List<Double> scores = entry.getValue();
            double average = calculateAverage(scores);

            averageScores.put(nutrient, average);
        }

        return averageScores;
    }

    private double calculateAverage(List<Double> scores) {
        return scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public List<Nutrient> findHighScoringNutrients(Map<Nutrient, Double> averageScores) {
        List<Nutrient> highScoringNutrients = new ArrayList<>();

        for (Map.Entry<Nutrient, Double> entry : averageScores.entrySet()) {
            Nutrient nutrient = entry.getKey();
            Double averageScore = entry.getValue();

            if (averageScore >= 2.3) {
                highScoringNutrients.add(nutrient);
            }
        }

        return highScoringNutrients;
    }

    public List<Nutrient> findTopHighScoringNutrients(Map<Nutrient, Double> averageScores, int topCount) {
        List<Nutrient> highScoringNutrients = new ArrayList<>();

        averageScores.entrySet().stream()
                .filter(entry -> entry.getValue() >= 3.0)
                .sorted(Map.Entry.<Nutrient, Double>comparingByValue().reversed())
                .limit(topCount)
                .map(Map.Entry::getKey)
                .forEach(highScoringNutrients::add);

        return highScoringNutrients;
    }
}

