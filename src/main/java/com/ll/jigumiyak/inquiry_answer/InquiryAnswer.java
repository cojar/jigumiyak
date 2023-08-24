package com.ll.jigumiyak.inquiry_answer;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.inquiry.Inquiry;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InquiryAnswer extends BaseEntity {

    private String content;

    @ManyToOne
    private SiteUser author;

    @ManyToOne
    private Inquiry inquiry;
}
