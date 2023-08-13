package com.ll.jigumiyak.nutrient_caution;

import com.ll.jigumiyak.nutrient.Nutrient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
public class NutrientCaution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String caution;

    @ManyToMany(mappedBy = "cautionList")
    private List<Nutrient> nutrients;
}
