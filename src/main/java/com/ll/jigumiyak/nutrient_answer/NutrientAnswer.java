package com.ll.jigumiyak.nutrient_answer;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.survey_answer.SurveyAnswer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NutrientAnswer extends BaseEntity {
    @Column
    private double score;
    @ManyToOne
    private SurveyAnswer answer;
    @ManyToOne
    private Nutrient nutrient;
}
