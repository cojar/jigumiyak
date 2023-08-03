package com.ll.jigumiyak.notice_comment;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeCommentForm {
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
