package com.ll.jigumiyak.ksyTest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.channels.AlreadyBoundException;

@Component
public class DataLoader implements CommandLineRunner {

    private final NutrientCategoryRepository nutrientCategoryRepository;

    public DataLoader(NutrientCategoryRepository nutrientCategoryRepository) {
        this.nutrientCategoryRepository = nutrientCategoryRepository;
    }

    @Override
    public void run(String... args) {
        if (nutrientCategoryRepository.findAll().isEmpty()) {
            // nutrientCategory 데이터 생성 및 저장
            String nutrientCategories = "눈 체력 체지방 뼈 항산화 간 혈액 순환 장 스트레스 갱년기 피부 혈압 수면 면역력 관절 혈당 뇌 전립선 위배뇨 근력 기타";
            String[] categories = nutrientCategories.split(" ");
            for (String category : categories) {
                NutrientCategory nutrientCategory = new NutrientCategory();
                nutrientCategory.setCategoryName(category);
                nutrientCategoryRepository.save(nutrientCategory);
            }
        } else {
            System.out.println("이미 있슴다");
        }
    }
}