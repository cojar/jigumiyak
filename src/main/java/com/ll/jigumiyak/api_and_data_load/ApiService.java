package com.ll.jigumiyak.api_and_data_load;

import com.fasterxml.jackson.databind.JsonNode;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientRepository;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import com.ll.jigumiyak.nutrient_caution.NutrientCaution;
import com.ll.jigumiyak.nutrient_caution.NutrientCautionRepository;
import com.ll.jigumiyak.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final ProductRepository productRepository;
    private final NutrientRepository nutrientRepository;
    private final NutrientCategoryRepository nutrientCategoryRepository;
    private final NutrientCautionRepository nutrientCautionRepository;

    public List<NutrientCategory> extractEfficacyList(String fncltyCn) {
        List<NutrientCategory> categoryList = new ArrayList<>();
        if (fncltyCn != null) {
            //로직을 짜자
            for (NutrientCategory category : nutrientCategoryRepository.findAll()) {
                if (fncltyCn.contains(category.getCategoryName())) {
                    categoryList.add(category);
                }
            }
            if(categoryList.size() == 0) {
                NutrientCategory category = nutrientCategoryRepository.findById(nutrientCategoryRepository.count()).get();
                categoryList.add(category);
                return categoryList;
            }
        }
        return categoryList;
    }

    public void saveNutrient(String name, String efficacy, String dailyIntake, List<NutrientCategory> nutrientCategoryList, List<NutrientCaution> cautionList) {
        // 중복 여부를 데이터베이스에서 확인
        boolean isDuplicate = checkIfNutrientExists(name);

        if (!isDuplicate) {
            Nutrient nutrient = Nutrient.builder()
                    .name(name)
                    .efficacy(efficacy)
                    .dailyIntake(dailyIntake)
                    .categoryList(nutrientCategoryList)
                    .cautionList(cautionList)
                    .build();

            this.nutrientRepository.save(nutrient);
        } else {
            System.out.println("이미 중복된 데이터입니다: " + name);
        }
    }

    private boolean checkIfNutrientExists(String name) {
        return nutrientRepository.existsByName(name);
    }

    public List<NutrientCaution> extractCautionList(String iftknAtntMatrCn) {
        List<NutrientCaution> cautionList = new ArrayList<>();
        if (iftknAtntMatrCn != null) {
            //로직을 짜자
            for (NutrientCaution caution : nutrientCautionRepository.findAll()) {
                if (iftknAtntMatrCn.contains(caution.getCaution())) {
                    cautionList.add(caution);
                }
            }
        }
        return cautionList;
    }


    // 괄호와 괄호안의 내용 지우는 함수
    public String deleteBracketAndStringInBracket(String text){
        Pattern pattern_bracket = Pattern.compile("\\([^\\(\\)]+\\)");

        Matcher matcher = pattern_bracket.matcher(text);
        String pureText = text;
        String removeText = new String();
        while(matcher.find()){
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            removeText = pureText.substring(startIndex, endIndex);
            pureText = pureText.replace(removeText, "");
            matcher = pattern_bracket.matcher(pureText);
        }
        return pureText;
    }

    public String deleteEng(String pureTextContainKorEng){
        String fnclty = pureTextContainKorEng.replaceAll("[a-zA-Z]", "");
        return fnclty;
    }

    public void processNutrientData1(JsonNode rowNode) {
        for (JsonNode dataNode : rowNode) {
            String aplcRawmtrlNm = dataNode.get("APLC_RAWMTRL_NM").asText();
            String fncltyCn = dataNode.get("FNCLTY_CN").asText();
            String dayIntkCn = dataNode.get("DAY_INTK_CN").asText();
            String iftknAtntMatrCn = dataNode.get("IFTKN_ATNT_MATR_CN").asText();

            List<NutrientCategory> nutrientCategoryList = extractEfficacyList(fncltyCn);
            List<NutrientCaution> cautionList = extractCautionList(iftknAtntMatrCn);

            saveNutrient(aplcRawmtrlNm, fncltyCn, dayIntkCn, nutrientCategoryList, cautionList);
        }
    }

    public void processNutrientData2(JsonNode dataNode) {
        String prdctNm = dataNode.get("PRDCT_NM").asText();
        String caution = dataNode.get("IFTKN_ATNT_MATR_CN").asText();
        String primaryFnclty = dataNode.get("PRIMARY_FNCLTY").asText();
        String dayIntkLowlimit = dataNode.get("DAY_INTK_LOWLIMIT").asText();
        String dayIntkHighlimit = dataNode.get("DAY_INTK_HIGHLIMIT").asText();
        String intkUnit = dataNode.get("INTK_UNIT").asText();

        String nutrientNm = deleteBracketAndStringInBracket(prdctNm);
        String efficacy_del_bracket = deleteBracketAndStringInBracket(primaryFnclty);
        String efficacy = deleteEng(efficacy_del_bracket);
        List<NutrientCategory> nutrientCategoryList = extractEfficacyList(efficacy);
        List<NutrientCaution> cautionList = extractCautionList(caution);

        saveNutrient(nutrientNm, efficacy, dayIntkLowlimit + "~" + dayIntkHighlimit + intkUnit, nutrientCategoryList, cautionList);
    }

}
