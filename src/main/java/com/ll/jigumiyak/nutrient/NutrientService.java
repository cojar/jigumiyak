package com.ll.jigumiyak.nutrient;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NutrientService {
    private final NutrientRepository nutrientRepository;
    private final NutrientCategoryRepository nutrientCategoryRepository;

    public void saveNutrient(Nutrient nutrient) {
        nutrientRepository.save(nutrient);
    }

    public List<Nutrient> getList() {
        return nutrientRepository.findAll();
    }

    public Nutrient findByName(String name) {
        Optional<Nutrient> nutrient = nutrientRepository.findByName(name);
        if(nutrient.isPresent()){
            return nutrient.get();
        } else {
            throw new DataNotFoundException("nutrient not found");
        }
    }
}
