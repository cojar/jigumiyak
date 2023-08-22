package com.ll.jigumiyak.faq;

import com.ll.jigumiyak.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Faq extends BaseEntity {

    private String question;

    private String answer;

    private String category;
}
