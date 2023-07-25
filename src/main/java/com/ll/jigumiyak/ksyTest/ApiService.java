package com.ll.jigumiyak.ksyTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ll.jigumiyak.product.Product;
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
    @Value("${api.address}")
    private String apiAddress;

    @Value("${api.auth.key}")
    private String apiKey;

    public String fetchDataFromApi() {
        JsonObject jsonObject = new JsonObject();
        // Make API request using RestTemplate or any other HTTP client library
        // In this example, RestTemplate is used to make the GET request
        RestTemplate restTemplate = new RestTemplate();
        String url = apiAddress + "?key=" + apiKey;

        // Perform API call and get the JSON response
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
