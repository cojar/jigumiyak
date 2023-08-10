package com.ll.jigumiyak.nutrient_caution;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NutrientCaution {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String caution;
}
