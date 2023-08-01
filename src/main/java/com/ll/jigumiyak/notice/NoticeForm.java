package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.notice_category.NoticeCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeForm {
    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=300)
    private String title;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;

    @NotEmpty(message="카테고리 선택은 필수항목입니다.")
    private String category;
}
