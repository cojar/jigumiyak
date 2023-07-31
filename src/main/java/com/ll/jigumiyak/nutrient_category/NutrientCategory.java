package com.ll.jigumiyak.nutrient_category;

import com.ll.jigumiyak.nutrient.Nutrient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class NutrientCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String categoryName;
    //눈 체력 체지방 뼈 등등
    @ManyToOne
    private Nutrient nutrient;
}
