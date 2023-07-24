package com.ll.jigumiyak.ksyTest;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;

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
}
