package com.ll.jigumiyak.nutrient_category;

import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NutrientCategoryService {
    private final NutrientCategoryRepository nutrientCategoryRepository;
    public List<String> getNutrientNameList(){
        List<String> nameList = new ArrayList<>();
        List<NutrientCategory> categories = nutrientCategoryRepository.findAll();
        for(NutrientCategory category : categories){
            nameList.add(category.getCategoryName());
        }
        return nameList;
    }
}
