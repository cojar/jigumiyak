package com.ll.jigumiyak.nutrient_caution;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.nutrient.Nutrient;
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
public class NutrientCaution extends BaseEntity {

    @Column
    private String caution;

    @ManyToMany(mappedBy = "cautionList")
    private List<Nutrient> nutrients;
}
