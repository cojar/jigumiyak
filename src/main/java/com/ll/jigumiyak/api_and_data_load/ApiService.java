package com.ll.jigumiyak.api_and_data_load;

import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientRepository;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import com.ll.jigumiyak.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final ProductRepository productRepository;
    private final NutrientRepository nutrientRepository;
    private final NutrientCategoryRepository nutrientCategoryRepository;
    @Value("${api.address}")
    private String apiAddress;

    @Value("${api.auth.key}")
    private String apiKey;

    public String fetchDataFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = apiAddress + "?key=" + apiKey;

        String jsonResponse = restTemplate.getForObject(url, String.class);

        return jsonResponse;
    }

    public NutrientCategory extractEfficacy(String fncltyCn) {
        NutrientCategory nutrientCategory = new NutrientCategory();
        if (fncltyCn != null) {
            //로직을 짜자
            for (NutrientCategory category : nutrientCategoryRepository.findAll()) {
                if (fncltyCn.contains(category.getCategoryName())) {
                    return category;
                } else {
                    category = nutrientCategoryRepository.findById(nutrientCategoryRepository.count()).get();
                    return category;
                }
            }
        }
        return nutrientCategory;
    }

    public void saveNutrient(String name, String efficacy) {
        Nutrient nutrient = new Nutrient();
        nutrient.setName(name);
        nutrient.setEfficacy(efficacy);
        nutrientRepository.save(nutrient);
    }
}
