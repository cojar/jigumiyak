package com.ll.jigumiyak.board_recomment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRecommentForm {
    @NotEmpty(message = "내용은 필수항목입니다.")
    @Size(max = 200, message ="내용을 200자 이내로 작성해주세요." )
    private String content;
}
