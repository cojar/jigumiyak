package com.ll.jigumiyak.survey;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.survey_answer.SurveyAnswer;
import com.ll.jigumiyak.user.SiteUser;
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
public class Survey extends BaseEntity {
    private String question;
    @OneToMany
    private List<SurveyAnswer> surveyAnswerList;
    @ManyToOne
    private SiteUser surveyee;
    @ManyToOne
    private NutrientCategory nutrientCategory;
}
