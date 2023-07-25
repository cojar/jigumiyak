package com.ll.jigumiyak.ksyTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ApiCaller {

    public static void main(String[] args) {
        // API 엔드포인트(URL) 설정
        String apiUrl = "http://openapi.foodsafetykorea.go.kr/api/sample/I-0040/json/1/5"; // api주소 입력

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

                // JSON 배열을 하나씩 처리
                for (JsonNode dataNode : rowNode) {
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
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("API 호출이 실패하였습니다. 상태 코드: " + responseEntity.getStatusCode());
        }
    }
}