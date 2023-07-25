package com.ll.jigumiyak.ksyTest;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {

    @Value("${api.address}")
    private String apiAddress;

    @Value("${api.auth.key}")
    private String apiKey;

    public String fetchDataFromApi() {
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

        ApiResponse[] apiResponses = new Gson().fromJson(apiResponseJson, ApiResponse[].class);

        // ApiResponse 배열을 순회하며 필요한 단어를 추출
        for (ApiResponse response : apiResponses) {
            String primaryFunctionality = response.getFNCLTY_CN(); // 필드 이름이 바뀜?
            if (primaryFunctionality != null) {
                String[] words = primaryFunctionality.split("\\s+");
                //로직을 짜자
                if (words.equals("기억력"))

                if (words.length > 0) {
                    mostNecessaryWords.add(words[0]);
                }
            }
        }
        System.out.println(mostNecessaryWords);

        return mostNecessaryWords;
    }
}
