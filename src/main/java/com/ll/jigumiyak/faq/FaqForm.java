package com.ll.jigumiyak.faq;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqForm {
    @NotEmpty(message="제목 필수항목")
    private String question;

    @NotEmpty(message="답변 필수항목")
    private String answer;

    @NotEmpty(message="카테고리 필수항목")
    private String category;
}
