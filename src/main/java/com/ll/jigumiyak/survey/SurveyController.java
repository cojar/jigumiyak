package com.ll.jigumiyak.survey;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientService;
import com.ll.jigumiyak.nutrient_answer.NutrientAnswerService;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryService;
import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.product.ProductService;
import com.ll.jigumiyak.survey_answer.SurveyAnswer;
import com.ll.jigumiyak.survey_answer.SurveyAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    private final ProductService productService;

    @PreAuthorize("hasAuthority('admin')")
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

    @GetMapping("/ready")
    public String beforeSurvey() {
        return "survey_ready";
    }


    @GetMapping("")
    public String doSurvey(Model model,
                           SurveyForm surveyForm,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        Page<Survey> surveyList = surveyService.getSurveyList(page, pageSize);
        model.addAttribute("surveyList", surveyList);
        return "survey";
    }

    //    @PostMapping("/submit")
//    public String submitSurvey(@RequestBody Map<Long, Long> answerIdMap, Model model,
//                                       RedirectAttributes redirectAttributes) {
//        List<Nutrient> highestNutrients = surveyService.getNutrientsWithHighestScores(answerIdMap);
//        redirectAttributes.addFlashAttribute("highestNutrients",highestNutrients);
//       return "redirect:/survey/result";
//    }
    @PostMapping("/submit")
    public String submitSurvey(@ModelAttribute("surveyForm") @Valid SurveyForm surveyForm,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "survey";
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<Long, Long> answerIdMap = new HashMap<>();
        try {
            answerIdMap = objectMapper.readValue(surveyForm.getAnswerIdMap(), new TypeReference<Map<Long, Long>>() {
            });
        } catch (IOException e) {

        }
        Map<Nutrient, Double> avgNutrientMap = surveyService.getAverageScoresByNutrient(answerIdMap);
        redirectAttributes.addFlashAttribute("avgNutrientMap", avgNutrientMap);
        List<Nutrient> highestNutrients = surveyService.findHighScoringNutrients(avgNutrientMap);
        redirectAttributes.addFlashAttribute("highestNutrients", highestNutrients);
        List<Nutrient> top3Nutrients = surveyService.findTopHighScoringNutrients(avgNutrientMap, 3);
        redirectAttributes.addFlashAttribute("top3Nutrients", top3Nutrients);
        return "redirect:/survey/result";
    }

    @GetMapping("/result")
    public String success(@ModelAttribute("highestNutrients") ArrayList<Nutrient> highestNutrients,
                          @ModelAttribute("avgNutrientMap") Map<Nutrient, Double> avgNutrientMap,
                          @ModelAttribute("top3Nutrients") ArrayList<Nutrient> top3Nutrients,
                          Model model) {

        List<Product> products = highestNutrients.stream()
                .map(nutrient -> productService.findProductsByNutrientName(nutrient.getName()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("products", products);
        model.addAttribute("highestNutrients", highestNutrients);
        model.addAttribute("avgNutrientMap", avgNutrientMap);
        model.addAttribute("top3Nutrients", top3Nutrients);
        for (Nutrient nutrient : highestNutrients) {
            System.out.println(nutrient.getName());
        }
        return "survey_result";
    }
}
