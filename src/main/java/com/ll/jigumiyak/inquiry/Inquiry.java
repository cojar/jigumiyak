package com.ll.jigumiyak.inquiry;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.file.GenFile;
import com.ll.jigumiyak.inquiry_answer.InquiryAnswer;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Inquiry extends BaseEntity {

    private String subject;

    private String content;

    private boolean email;

    private String category;

    private boolean state;

    @ManyToOne
    private SiteUser inquirer;

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.REMOVE)
    private List<InquiryAnswer> answerList = new ArrayList<>();

    @OneToOne
    private GenFile img;
}
