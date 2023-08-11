package com.ll.jigumiyak.api_and_data_load;

import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientRepository;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import com.ll.jigumiyak.nutrient_caution.NutrientCaution;
import com.ll.jigumiyak.nutrient_caution.NutrientCautionRepository;
import com.ll.jigumiyak.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final ProductRepository productRepository;
    private final NutrientRepository nutrientRepository;
    private final NutrientCategoryRepository nutrientCategoryRepository;
    private final NutrientCautionRepository nutrientCautionRepository;

    public List<NutrientCategory> extractEfficacyList(String fncltyCn) {
        List<NutrientCategory> categoryList = new ArrayList<>();
        if (fncltyCn != null) {
            //로직을 짜자
            for (NutrientCategory category : nutrientCategoryRepository.findAll()) {
                if (fncltyCn.contains(category.getCategoryName())) {
                    categoryList.add(category);
                }
            }
            if(categoryList.isEmpty()) {
                NutrientCategory category = nutrientCategoryRepository.findById(nutrientCategoryRepository.count()).get();
                categoryList.add(category);
                return categoryList;
            }
        }
        return categoryList;
    }

    public void saveNutrient(String name, String efficacy, String dailyIntake, List<NutrientCategory> nutrientCategoryList, List<NutrientCaution> cautionList) {
        Nutrient nutrient = new Nutrient();
        nutrient.setName(name);
        nutrient.setEfficacy(efficacy);
        nutrient.setDailyIntake(dailyIntake);
        nutrient.setCategoryList(nutrientCategoryList);
        nutrient.setCautionList(cautionList);
        nutrientRepository.save(nutrient);
    }

    public String extractDailyIntake(String dayIntkCn){
        String[] param = dayIntkCn.split("\\/");
        String dailyIntake = param[0].replaceAll("[^0-9, ^mg, ^g, ^kg]", "").trim().replaceAll(" ", "");
        return dailyIntake;
    }

    public List<NutrientCaution> extractCautionList(String iftknAtntMatrCn) {
        List<NutrientCaution> cautionList = new ArrayList<>();
        if (iftknAtntMatrCn != null) {
            //로직을 짜자
            for (NutrientCaution caution : nutrientCautionRepository.findAll()) {
                if (iftknAtntMatrCn.contains(caution.getCaution())) {
                    cautionList.add(caution);
                }
            }
        }
        return cautionList;
    }
}
