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

        List<String> mostNecessaryWords = apiService.extractMostNecessaryWords(apiResponseJson);

        return new ResponseEntity<>(mostNecessaryWords, HttpStatus.OK);
    }
}