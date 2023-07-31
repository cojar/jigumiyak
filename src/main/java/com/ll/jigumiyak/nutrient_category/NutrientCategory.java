package com.ll.jigumiyak.ksyTest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
public class NutrientCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String categoryName;
    //눈 체력 체지방 뼈 등등
    @ManyToOne
    private Nutrient nutrient;
}
