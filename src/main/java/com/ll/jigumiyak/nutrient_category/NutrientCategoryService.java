package com.ll.jigumiyak.nutrient_category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NutrientCategoryService {

    private final NutrientCategoryRepository nutrientCategoryRepository;

    public List<NutrientCategory> getList() {
        return nutrientCategoryRepository.findAll();
    }
}
