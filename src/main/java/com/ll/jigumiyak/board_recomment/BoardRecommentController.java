package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.board_comment.BoardCommentService;
import com.ll.jigumiyak.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/brecomment")
@RequiredArgsConstructor
@Controller
public class BoardRecommentController {
    private final BoardCommentService boardCommentService;
    private final BoardRecommentService boardRecommentService;
    private final UserService userService;
}
