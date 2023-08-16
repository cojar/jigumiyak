package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.board_comment.BoardCommentService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/brecomment")
@RequiredArgsConstructor
@Controller
public class BoardRecommentController {
    private final BoardCommentService boardCommentService;
    private final BoardRecommentService boardRecommentService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createRecomment(@PathVariable("id") Long id, @Valid BoardRecommentForm boardRecommentForm,
                               BindingResult bindingResult, Principal principal) {
        BoardComment comment = this.boardCommentService.getBoardComment(id);
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());

        this.boardRecommentService.create(comment, boardRecommentForm.getContent(), siteUser);
        return String.format("redirect:/board/%s#comment_%s", comment.getBoard().getId(), comment.getId());
    }

}
