package com.ll.jigumiyak.survey;

import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientService;
import com.ll.jigumiyak.nutrient_answer.NutrientAnswer;
import com.ll.jigumiyak.nutrient_answer.NutrientAnswerService;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryService;
import com.ll.jigumiyak.survey_answer.SurveyAnswer;
import com.ll.jigumiyak.survey_answer.SurveyAnswerService;
import com.ll.jigumiyak.util.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/survey")
@Controller
@RequiredArgsConstructor
public class SurveyController {
    private final NutrientCategoryService nutrientCategoryService;
    private final SurveyAnswerService surveyAnswerService;
    private final SurveyService surveyService;
    private final NutrientService nutrientService;
    private final NutrientAnswerService nutrientAnswerService;

    @GetMapping("/create")
    @ResponseBody
    public String createSurvey() {
        surveyService.createSurvey("골다공증 발생 위험을 줄이고 싶다", nutrientCategoryService.getCategoryByCategoryName("뼈"));
        return "";
    }

    @GetMapping("/answer1")
    @ResponseBody
    public String createSurveyAnswer() {
        surveyAnswerService.createSurveyAnswer("그렇지 않다", surveyService.getSurvey(7L));
        return "";
    }

    @GetMapping("/answer2")
    @ResponseBody
    public String createSurveyAnswer2() {
        surveyAnswerService.createSurveyAnswer("약간 그렇다", surveyService.getSurvey(7L));
        return "";
    }

    @GetMapping("/answer3")
    @ResponseBody
    public String createSurveyAnswer3() {
        surveyAnswerService.createSurveyAnswer("그렇것 같다", surveyService.getSurvey(7L));
        return "";
    }

    @GetMapping("/answer4")
    @ResponseBody
    public String createSurveyAnswer4() {
        surveyAnswerService.createSurveyAnswer("매우 그렇다", surveyService.getSurvey(7L));
        return "";
    }

    @GetMapping("/nutrient/answer1")
    @ResponseBody
    public String createNutrientAnswer1() {

        List<Nutrient> nutrientList = surveyService.getNutrientListBySurvey(7L);
        SurveyAnswer surveyAnswer = surveyAnswerService.getSurveyAnswer(24L);

        Map<Nutrient, Double> nutrientScores = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println(surveyAnswer.getSurvey().getNutrientCategory().getCategoryName() + "의 "
                + surveyAnswer.getSurvey().getQuestion() + " 문항 중 " + surveyAnswer.getAnswerText() + "의 영양제 점수에 대해 입력하는 부분");
        for (Nutrient nutrient : nutrientList) {
            System.out.println("문제에 해당하는 각 영양성분의 점수 " + nutrient.getName() + ": ");
            double score = scanner.nextDouble();
            nutrientScores.put(nutrient, score);
        }
        System.out.println("모두 입력완료");
        scanner.close();
        nutrientAnswerService.createNutrientAnswer(nutrientScores, surveyAnswer);
        return "nutrientAnswerList are created";
    }

    @GetMapping("")
    public String doSurvey(Model model,
                           SurveyForm surveyForm,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        Page<Survey> surveyList = surveyService.getSurvetList(page, pageSize);
        model.addAttribute("surveyList", surveyList);
        return "survey";
    }

    @PostMapping("/submit")
    public String submitSurvey(@RequestBody Map<Long, Long> answerIdMap, Model model) {
        for (Map.Entry<Long, Long> entry : answerIdMap.entrySet()) {
            Long questionId = entry.getKey();
            Long answerId = entry.getValue();
            System.out.println("설문 질문 ID: " + questionId + ", 설문 질문 답변 ID: " + answerId);
        }

        List<Nutrient> highestNutrients = surveyService.getNutrientsWithHighestScores(answerIdMap);

        // 가장 높은 점수를 가진 영양성분 출력
        for (Nutrient nutrient : highestNutrients) {
            System.out.println("나와라 좀: " + nutrient.getName());
        }
        model.addAttribute("highestNutrients", highestNutrients);

       return "recommended_result";
    }
}
