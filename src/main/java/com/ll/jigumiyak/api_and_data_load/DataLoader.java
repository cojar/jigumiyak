package com.ll.jigumiyak.api_and_data_load;

import com.ll.jigumiyak.notice_category.NoticeCategory;
import com.ll.jigumiyak.notice_category.NoticeCategoryRepository;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import com.ll.jigumiyak.nutrient_caution.NutrientCaution;
import com.ll.jigumiyak.nutrient_caution.NutrientCautionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final NutrientCategoryRepository nutrientCategoryRepository;
    private final NoticeCategoryRepository noticeCategoryRepository;
    private final NutrientCautionRepository nutrientCautionRepository;

    @Override
    public void run(String... args) {
        if (nutrientCategoryRepository.findAll().isEmpty()) {
            // nutrientCategory 데이터 생성 및 저장
            String nutrientCategories = "눈 체력 체지방 뼈 항산화 간 혈액 순환 장 위 스트레스 갱년기 피부 혈압 수면 면역력 관절 혈당 뇌 전립선 위배뇨 근력 기타";
            String[] categories = nutrientCategories.split(" ");
            for (String category : categories) {

                NutrientCategory nutrientCategory = NutrientCategory.builder()
                        .categoryName(category)
                        .build();

                log.info("category: ", category);

                this.nutrientCategoryRepository.save(nutrientCategory);
            }
        } else {
            log.info("영양성분은 이미 생성 되어있습니다");
        }

        if(noticeCategoryRepository.findAll().isEmpty()){

            NoticeCategory noticeCategory1 = NoticeCategory.builder()
                    .name("공지사항")
                    .build();

            this.noticeCategoryRepository.save(noticeCategory1);

            NoticeCategory noticeCategory2 = NoticeCategory.builder()
                    .name("이벤트")
                    .build();

            this.noticeCategoryRepository.save(noticeCategory2);
        }
        else {
            log.info("공지사항 카테고리는 이미 생성되어 있습니다");
        }

        if (nutrientCautionRepository.findAll().isEmpty()) {
            // nutrientCaution 데이터 생성 및 저장
            String cautions = "유아 영아 노약자 심약자 임산부 수유부 알레르기";
            String[] cautionList = cautions.split(" ");
            for (String caution : cautionList) {

                NutrientCaution nutrientCaution = NutrientCaution.builder()
                        .caution(caution)
                        .build();

                log.info("caution: ", caution);

                this.nutrientCautionRepository.save(nutrientCaution);
            }
        } else {
            log.info("영양성분 주의사항은 이미 생성 되어있습니다");
        }
    }
}