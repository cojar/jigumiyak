package com.ll.jigumiyak.nutrient_category;

import com.ll.jigumiyak.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NutrientCategoryService {

    private final NutrientCategoryRepository nutrientCategoryRepository;

    public List<NutrientCategory> getList() {
        return nutrientCategoryRepository.findAll();
    }

    public NutrientCategory getCategoryByCategoryName(String categoryName) {
        Optional<NutrientCategory> optionalNutrientCategory = nutrientCategoryRepository.findByCategoryName(categoryName);
        if (optionalNutrientCategory.isPresent()) {
            return optionalNutrientCategory.get();
        } else {
            throw new DataNotFoundException("not found nutrientCategory");
        }
    }

    public NutrientCategory getNutrientCategoryBySurvey(Long id){
        Optional<NutrientCategory> optionalNutrientCategory = nutrientCategoryRepository.findBySurveyId(id);
        if(optionalNutrientCategory.isPresent()){
            return optionalNutrientCategory.get();
        } else {
            throw new DataNotFoundException("not found nutrientCategory");
        }
    }
}
