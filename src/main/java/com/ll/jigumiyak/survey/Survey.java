package com.ll.jigumiyak.survey;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Survey extends BaseEntity {

    @ManyToOne
    private SiteUser surveyee;
}
