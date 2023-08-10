package com.ll.jigumiyak.nutrient;

import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NutrientService {
    private final NutrientRepository nutrientRepository;
    private final NutrientCategoryRepository nutrientCategoryRepository;

    public void saveNutrient(Nutrient nutrient) {
        nutrientRepository.save(nutrient);
    }

    // 뭐하려고 이코드를 짰더라
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
