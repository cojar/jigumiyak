package com.ll.jigumiyak.api_and_data_load;

import com.google.gson.Gson;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientRepository;
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

    public List<String> extractMostNecessaryWords(String apiResponseJson) {
        List<String> mostNecessaryWords = new ArrayList<>();

        //ApiResponse[] apiResponses = new Gson().fromJson(apiResponseJson, ApiResponse[].class);
        Gson gson = new Gson();
        ApiResponse[] apiResponses = gson.fromJson(apiResponseJson,  ApiResponse[].class);


        // ApiResponse 배열을 순회하며 필요한 단어를 추출
        for (ApiResponse response : apiResponses) {
            String primaryFunctionality = response.getFNCLTY_CN(); // 필드 이름이 바뀜?
            if (primaryFunctionality != null) {
                String[] words = primaryFunctionality.split("\\s+");
                //로직을 짜자
                for (String word : words) {
                    if (word.contains("기억")) {
                        saveNutrient(response);
                    }
                }
            }
        }
        System.out.println(mostNecessaryWords);

        return mostNecessaryWords;
    }

    public void saveNutrient(ApiResponse response){
        Nutrient nutrient = new Nutrient();
        nutrient.setName(response.getAPLC_RAWMTRL_NM());
        nutrientRepository.save(nutrient);
    }
}
