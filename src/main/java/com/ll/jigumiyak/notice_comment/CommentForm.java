package com.ll.jigumiyak.notice_comment;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {
    @Valid
    private String content;
}
