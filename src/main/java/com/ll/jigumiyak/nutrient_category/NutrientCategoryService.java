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
}
