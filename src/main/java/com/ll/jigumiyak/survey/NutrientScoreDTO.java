package com.ll.jigumiyak.survey;

import com.ll.jigumiyak.nutrient.Nutrient;
import lombok.Getter;

@Getter
public class NutrientScoreDTO {
    private Nutrient nutrient;

    private double score;
    public NutrientScoreDTO(Nutrient nutrient, double score) {
        this.nutrient = nutrient;
        this.score = score;
    }
}
