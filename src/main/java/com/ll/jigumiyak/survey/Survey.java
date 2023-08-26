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
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<SurveyAnswer> surveyAnswerList;
    @ManyToOne
    private SiteUser surveyee;
    @ManyToMany
    @JoinTable(name = "survey_category_mapping",
            joinColumns = @JoinColumn(name = "survey_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<NutrientCategory> categoryList;
}
