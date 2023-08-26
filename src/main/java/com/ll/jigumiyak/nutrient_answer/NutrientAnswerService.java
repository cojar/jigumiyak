package com.ll.jigumiyak.nutrient_answer;

import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.survey_answer.SurveyAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class NutrientAnswerService {
    private final NutrientAnswerRepository nutrientAnswerRepository;


    public List<NutrientAnswer> createNutrientAnswer(Map<Nutrient, Double> nutrientScores, SurveyAnswer answer) {
        List<NutrientAnswer> nutrientAnswers = nutrientScores.entrySet().stream()
                .map(entry -> {
                    Nutrient nutrient = entry.getKey();
                    Double score = entry.getValue();

                    return NutrientAnswer.builder()
                            .nutrient(nutrient)
                            .score(score)
                            .answer(answer)
                            .build(); // This line was missing in your code
                })
                .collect(Collectors.toList());

        this.nutrientAnswerRepository.saveAll(nutrientAnswers);
        return nutrientAnswers;
    }
}
