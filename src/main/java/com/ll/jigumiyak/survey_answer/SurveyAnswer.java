package com.ll.jigumiyak.survey_answer;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient_answer.NutrientAnswer;
import com.ll.jigumiyak.survey.Survey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SurveyAnswer extends BaseEntity {
    @Column
    private String answerText;

    @ManyToOne
    private Survey survey;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<NutrientAnswer> nutrientAnswerList;
}
