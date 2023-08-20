package com.ll.jigumiyak.api_and_data_load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_caution.NutrientCaution;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ApiCaller {
    private final ApiService apiService;
    @Value("${api.address}")
    private String apiAddress;
    @Value("${api.auth.key}")
    private String apiKey;
    // 영양성분 api 1
    @GetMapping("/api/test/controller")
    @ResponseBody
    public String apiRead() {
        String apiUrl = "http://openapi.foodsafetykorea.go.kr/api/dcbb839b5b0a4c3d8237/I-0040/json/1/613";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode rowNode = rootNode.get("I-0040").get("row");
                apiService.processNutrientData1(rowNode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("API 호출이 실패하였습니다. 상태 코드: " + responseEntity.getStatusCode());
        }
        return "";
    }


    // 영양성분 api 2
    @GetMapping("/api/test/controller2")
    @ResponseBody
    public String apiRead2() {
        String apiUrl = "http://openapi.foodsafetykorea.go.kr/api/dcbb839b5b0a4c3d8237/I2710/json/1/421";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode rowNode = rootNode.get("I2710").get("row");
                for (JsonNode dataNode : rowNode) {
                    apiService.processNutrientData2(dataNode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("API 호출이 실패하였습니다. 상태 코드: " + responseEntity.getStatusCode());
        }
        return "";
    }
}