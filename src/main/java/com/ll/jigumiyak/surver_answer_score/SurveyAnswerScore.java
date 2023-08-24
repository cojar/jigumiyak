package com.ll.jigumiyak.surver_answer_score;

import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.survey_answer.SurveyAnswer;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class SurveyAnswerScore {
    @Column
    private double score;
    @OneToMany
    private List<SurveyAnswer> answerList;
    @ManyToMany
    @JoinTable(
            name = "answer_score_nutrient",
            joinColumns = @JoinColumn(name = "answer_score_id"),
            inverseJoinColumns = @JoinColumn(name = "nutrient_id")
    )
    private List<Nutrient> nutrientList;
}
