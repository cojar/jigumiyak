package com.ll.jigumiyak.nutrient;

import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_caution.NutrientCaution;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Nutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String efficacy;
    private String dailyIntake;
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
}
