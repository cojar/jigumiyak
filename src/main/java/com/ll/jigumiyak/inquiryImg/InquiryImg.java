package com.ll.jigumiyak.inquiryImg;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.file.GenFile;
import com.ll.jigumiyak.inquiry.Inquiry;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InquiryImg extends BaseEntity {
    @OneToOne
    private GenFile img;

    @ManyToOne
    private Inquiry inquiry;
}
