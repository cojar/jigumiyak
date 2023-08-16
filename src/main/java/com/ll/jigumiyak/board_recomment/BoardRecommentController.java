package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.board_comment.BoardCommentForm;
import com.ll.jigumiyak.board_comment.BoardCommentService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
@RequestMapping("/brecomment")
@RequiredArgsConstructor
@Controller
public class BoardRecommentController {
}
