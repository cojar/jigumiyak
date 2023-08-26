package com.ll.jigumiyak.survey;

import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientService;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryService;
import com.ll.jigumiyak.survey_answer.SurveyAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/survey")
@Controller
@RequiredArgsConstructor
public class SurveyController {
    private final NutrientCategoryService nutrientCategoryService;
    private final SurveyAnswerService surveyAnswerService;
    private final SurveyService surveyService;
    private final NutrientService nutrientService;

    @GetMapping("")
    @ResponseBody
    public String createSurvey() {
        surveyService.createSurvey("PC, 스마트폰, TV 등을 시청하는 시간을 선택해주세요", nutrientCategoryService.getCategoryByCategoryName("눈"));
        return "";
    }
    @GetMapping("/answer1")
    @ResponseBody
    public String createSurveyAnswer(){
        surveyAnswerService.createSurveyAnswer("1시간 이하로 시청합니다", surveyService.getSurvey(1L));
        return "";
    }
    @GetMapping("/answer2")
    @ResponseBody
    public String createSurveyAnswer2(){
        surveyAnswerService.createSurveyAnswer("1시간 이상 3시간 이하로 시청합니다", surveyService.getSurvey(1L));
        return "";
    }

    @GetMapping("/answer3")
    @ResponseBody
    public String createSurveyAnswer3(){
        surveyAnswerService.createSurveyAnswer("3시간 이상 5시간 이하로 시청합니다", surveyService.getSurvey(1L));
        return "";
    }

    @GetMapping("/answer4")
    @ResponseBody
    public String createSurveyAnswer4(){
        surveyAnswerService.createSurveyAnswer("5시간 이상 시청합니다", surveyService.getSurvey(1L));
        return "";
    }

    @GetMapping("/nutrient/answer1")
    @ResponseBody
    public String createNutrientAnswer(){

        List<Nutrient> nutrientList = nutrientService.getNutrientListByNutrientCategory(1L);
        for(Nutrient nutrient : nutrientList){
            System.out.println(nutrient.getName());
        }

        Map<Nutrient, Double> nutrientScores = new HashMap<>();
        Nutrient nutrient1 = nutrientService.getNutrient(1L);
        nutrientScores.put(nutrient1, 4.5);

        Nutrient nutrient2 = nutrientService.getNutrient(2L);
        nutrientScores.put(nutrient2, 3.8);
        return "";
    }
}
