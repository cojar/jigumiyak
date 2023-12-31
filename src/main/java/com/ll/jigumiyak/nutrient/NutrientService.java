package com.ll.jigumiyak.nutrient;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NutrientService {

    private final NutrientRepository nutrientRepository;
    private final NutrientCategoryRepository nutrientCategoryRepository;

    public void saveNutrient(Nutrient nutrient) {
        this.nutrientRepository.save(nutrient);
    }

    public List<Nutrient> getList() {
        return this.nutrientRepository.findAll();
    }

    public Nutrient findByName(String name) {
        Optional<Nutrient> nutrient = this.nutrientRepository.findByName(name);
        if(nutrient.isPresent()){
            return nutrient.get();
        } else {
            throw new DataNotFoundException("nutrient not found");
        }
    }

    public List<String> containingNutrientName(Map<String, String> paraMap) {
        return nutrientRepository.findNutrientByNutrientName(paraMap.get("searchWord"));
    }

    public Nutrient getNutrient(long id) {
        Optional<Nutrient> optionalNutrient = nutrientRepository.findById(id);
        if(optionalNutrient.isPresent()){
            return optionalNutrient.get();
        } else {
            throw new DataNotFoundException("not found nutrient");
        }
    }

    public List<Nutrient> getNutrientListByNutrientCategory(Long id){
        return nutrientRepository.findByNutrientCategoryId(id);
    }
}
