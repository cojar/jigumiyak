package com.ll.jigumiyak.inquiry_answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryAnswerForm {
    @NotEmpty(message="답변 필수항목")
    private String content;
}
