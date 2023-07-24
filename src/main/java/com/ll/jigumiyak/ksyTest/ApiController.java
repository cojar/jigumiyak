package com.ll.jigumiyak.ksyTest;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/data")
    public ResponseEntity<List<String>> getMostNecessaryWords() {
        // JSON 데이터 가져오기
        String apiResponseJson = apiService.fetchDataFromApi();

        List<String> mostNecessaryWords = extractMostNecessaryWords(apiResponseJson);

        return new ResponseEntity<>(mostNecessaryWords, HttpStatus.OK);
    }

    // 예시코드
    private List<String> extractMostNecessaryWords(String apiResponseJson) {
        List<String> mostNecessaryWords = new ArrayList<>();

        ApiResponse[] apiResponses = new Gson().fromJson(apiResponseJson, ApiResponse[].class);

        // ApiResponse 배열을 순회하며 필요한 단어를 추출
        for (ApiResponse response : apiResponses) {
            String primaryFunctionality = response.getPrimaryFnclty(); // 필드 이름이 바뀜?
            if (primaryFunctionality != null) {
                // 로직을 짜자
                String[] words = primaryFunctionality.split("\\s+");
                if (words.length > 0) {
                    mostNecessaryWords.add(words[0]);
                }
            }
        }
        System.out.println(mostNecessaryWords);

        return mostNecessaryWords;
    }
}