package com.ll.jigumiyak.nutrient_category;

import com.ll.jigumiyak.nutrient.Nutrient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    @ManyToMany(mappedBy = "categoryList")
    private List<Nutrient> nutrientList;
}
