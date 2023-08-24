package com.ll.jigumiyak.survey_answer;

import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.surver_answer_score.SurveyAnswerScore;
import com.ll.jigumiyak.survey.Survey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SurveyAnswer {
    @Column
    private String answerText;

    // Many-to-One 관계: 여러 답변은 하나의 질문에 속함
    @ManyToOne
    private Survey survey;
    @ManyToOne
    private Nutrient nutrient;
    @Column
    private double score;
}
