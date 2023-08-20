package com.ll.jigumiyak.nutrient_category;

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
public class NutrientCategory extends BaseEntity {

    @Column
    private String categoryName;

    //눈 체력 체지방 뼈 등등
    @ManyToMany(mappedBy = "categoryList")
    private List<Nutrient> nutrientList;
}
