package com.ll.jigumiyak.inquiry;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryForm {
    @NotEmpty(message="제목 필수항목")
    private String subject;

    @NotEmpty(message="내용 필수항목")
    private String content;

    @NotEmpty(message="카테고리 필수항목")
    private String category;

    private boolean email;

    private boolean state;
}
