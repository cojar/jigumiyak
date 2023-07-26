package com.ll.jigumiyak.ksyTest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NutrientService {
    private final NutrientRepository nutrientRepository;
    private final NutrientCategoryRepository nutrientCategoryRepository;

    public void saveNutrient(Nutrient nutrient) {
        extractNutrientEfficacy(nutrient);
        nutrientRepository.save(nutrient);
    }

    public void extractNutrientEfficacy(Nutrient nutrient) {
        String nge = nutrient.getEfficacy();
        List<NutrientCategory> categories = nutrientCategoryRepository.findAll();
        for (NutrientCategory category : categories) {
            if (category.getCategoryName().contains(nge)) {
                nutrient.setEfficacy(category.getCategoryName());
                return;
            } else {
                nutrient.setEfficacy("기타");
            }
        }
    }

}
