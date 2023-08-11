package com.ll.jigumiyak.api_and_data_load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_caution.NutrientCaution;
import groovy.transform.AutoImplement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ApiCaller {
    private final ApiService apiService;
    @Value("${api.address}")
    private String apiAddress;
    @Value("${api.auth.key}")
    private String apiKey;

    @GetMapping("/api/test/controller")
    @ResponseBody
    public String apiRead() {

        // API 엔드포인트(URL) 설정
        // String apiUrl = apiAddress + apiKey + "/I-0040/json/1/613";
        // http://openapi.foodsafetykorea.go.kr/api/dcbb839b5b0a4c3d8237/I-0040/json/1/613
        String apiUrl = "http://openapi.foodsafetykorea.go.kr/api/dcbb839b5b0a4c3d8237/I-0040/json/1/613";

        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // API 호출 및 응답 받기
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

        // JSON 데이터 파싱을 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // 응답 데이터 확인
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            try {
                // JSON 데이터 파싱하여 Java 객체로 변환
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode rowNode = rootNode.get("I-0040").get("row");

                System.out.println(rowNode);

                // JSON 배열을 하나씩 처리
                // 현재 dataNode가 영양제 객체로 사용될 거임
                for (JsonNode dataNode : rowNode) {
                    Nutrient nutrient = new Nutrient();
                    nutrient.setName(dataNode.get("APLC_RAWMTRL_NM").asText());

                    System.out.println("---------------------------------------");
                    String prmsDt = dataNode.get("PRMS_DT").asText();
                    String dayIntkCn = dataNode.get("DAY_INTK_CN").asText();
                    String iftknAtntMatrCn = dataNode.get("IFTKN_ATNT_MATR_CN").asText();
                    String hfFncltyMtralRcognNo = dataNode.get("HF_FNCLTY_MTRAL_RCOGN_NO").asText();
                    String bsshNm = dataNode.get("BSSH_NM").asText();
                    String fncltyCn = dataNode.get("FNCLTY_CN").asText();
                    String aplcRawmtrlNm = dataNode.get("APLC_RAWMTRL_NM").asText();
                    String indutyNm = dataNode.get("INDUTY_NM").asText();
                    String addr = dataNode.get("ADDR").asText();

                    // 필요한 정보들을 하나씩 추출하여 원하는 방식으로 사용 가능
                    System.out.println("PRMS_DT: " + prmsDt);
                    System.out.println("DAY_INTK_CN: " + dayIntkCn);
                    System.out.println("IFTKN_ATNT_MATR_CN: " + iftknAtntMatrCn);
                    System.out.println("HF_FNCLTY_MTRAL_RCOGN_NO: " + hfFncltyMtralRcognNo);
                    System.out.println("BSSH_NM: " + bsshNm);
                    System.out.println("FNCLTY_CN: " + fncltyCn);
                    System.out.println("APLC_RAWMTRL_NM: " + aplcRawmtrlNm);
                    System.out.println("INDUTY_NM: " + indutyNm);
                    System.out.println("ADDR: " + addr);
                    System.out.println("---------------------------------------");

                    // 영양성분을 통한 카테고리 가공 테스트
                    // (
//                    String test1 = "눈 간 위 갱년기";
//                    List<NutrientCategory> testnutrientCategoryList = apiService.extractEfficacyList(test1);
//                    // test
//                    for(NutrientCategory nutrientCategory : testnutrientCategoryList){
//                        nutrientCategory.getCategoryName();
//                        System.out.println(nutrientCategory.getCategoryName());
//                    }
//                    System.out.println("--------------------------------------");
                    // )

                    // 영양성분을 통한 카테고리 가공
                    List<NutrientCategory> nutrientCategoryList = apiService.extractEfficacyList(fncltyCn);
                    for(NutrientCategory nutrientCategory : nutrientCategoryList){
                        nutrientCategory.getCategoryName();
                        System.out.println(nutrientCategory.getCategoryName());
                    }
                    String dailyIntake  = apiService.extractDailyIntake(dayIntkCn);
                    System.out.println(dailyIntake);

                    // 영양성분의 주의사항
                    List<NutrientCaution> cautionList = apiService.extractCautionList(iftknAtntMatrCn);
                    for(NutrientCaution caution : cautionList){
                        caution.getCaution();
                        System.out.println(caution.getCaution());
                    }
                    // 영양성분 저장
                    apiService.saveNutrient(aplcRawmtrlNm, fncltyCn, dailyIntake, nutrientCategoryList, cautionList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("API 호출이 실패하였습니다. 상태 코드: " + responseEntity.getStatusCode());
        }
        return "";
    }

    @GetMapping("/api/list/data")
    @ResponseBody
    public String apiListData(){
        List<NutrientCategory> categoryList = new ArrayList<>();
        return "";
    }
}