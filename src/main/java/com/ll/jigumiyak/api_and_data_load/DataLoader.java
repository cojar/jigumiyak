package com.ll.jigumiyak.api_and_data_load;

import com.ll.jigumiyak.notice_category.NoticeCategory;
import com.ll.jigumiyak.notice_category.NoticeCategoryRepository;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final NutrientCategoryRepository nutrientCategoryRepository;
    private final NoticeCategoryRepository noticeCategoryRepository;

    public DataLoader(NutrientCategoryRepository nutrientCategoryRepository, NoticeCategoryRepository noticeCategoryRepository) {
        this.nutrientCategoryRepository = nutrientCategoryRepository;
        this.noticeCategoryRepository = noticeCategoryRepository;
    }

    @Override
    public void run(String... args) {
        if (nutrientCategoryRepository.findAll().isEmpty()) {
            // nutrientCategory 데이터 생성 및 저장
            String nutrientCategories = "눈 체력 체지방 뼈 항산화 간 혈액 순환 장 위 스트레스 갱년기 피부 혈압 수면 면역력 관절 혈당 뇌 전립선 위배뇨 근력 기타";
            String[] categories = nutrientCategories.split(" ");
            for (String category : categories) {
                NutrientCategory nutrientCategory = new NutrientCategory();
                nutrientCategory.setCategoryName(category);
                System.out.println(category);
                nutrientCategoryRepository.save(nutrientCategory);
            }
        } else {
            System.out.println("영양성분은 이미 생성 되어있습니다");
        }

        if(noticeCategoryRepository.findAll().isEmpty()){
            NoticeCategory noticeCategory1 = new NoticeCategory();
            noticeCategory1.setName("공지사항");
            noticeCategoryRepository.save(noticeCategory1);

            NoticeCategory noticeCategory2 = new NoticeCategory();
            noticeCategory2.setName("이벤트");
            noticeCategoryRepository.save(noticeCategory2);
        }
        else {
            System.out.println("공지사항 카테고리는 이미 생성되어 있습니다");
        }
    }
}