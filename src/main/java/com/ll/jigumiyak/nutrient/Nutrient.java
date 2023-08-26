package com.ll.jigumiyak.nutrient;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_caution.NutrientCaution;
import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.nutrient_answer.NutrientAnswer;
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
public class Nutrient extends BaseEntity {

    private String name;

    private String efficacy;

    private String dailyIntake;
    @OneToMany(mappedBy = "nutrient", cascade = CascadeType.ALL)
    private List<NutrientAnswer> nutrientAnswerList;

    @ManyToMany
    @JoinTable(name = "nutrient_category_mapping",
            joinColumns = @JoinColumn(name = "nutrient_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<NutrientCategory> categoryList;

    @ManyToMany
    @JoinTable(name = "nutrient_caution_mapping",
            joinColumns = @JoinColumn(name = "nutrient_id"),
            inverseJoinColumns = @JoinColumn(name = "caution_id"))
    private List<NutrientCaution> cautionList;

    @ManyToMany(mappedBy = "nutrientList")
    private List<Product> productList;
}
