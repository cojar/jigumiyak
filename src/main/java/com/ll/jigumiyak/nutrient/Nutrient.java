package com.ll.jigumiyak.nutrient;

import com.ll.jigumiyak.nutrient_category.NutrientCategory;
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
    private Integer id;
    private String name;
    private String efficacy;
    private String dailyIntake;
    private String caution;
    @OneToMany(mappedBy = "nutrient", cascade = CascadeType.REMOVE)
    private List<NutrientCategory> categoryList;
}
