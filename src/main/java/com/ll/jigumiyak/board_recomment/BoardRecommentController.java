package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.board_comment.BoardCommentForm;
import com.ll.jigumiyak.board_comment.BoardCommentService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String recommentModify(Model model, BoardRecommentForm boardRecommentForm, @PathVariable("id") Long id, Principal principal) {
        BoardRecomment boardRecomment = this.boardRecommentService.getBoareRecomment(id);
        model.addAttribute("boardRecomment", boardRecomment);

        if (!boardRecomment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        boardRecommentForm.setContent(boardRecomment.getContent());
        return "brecomment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String recommentModify(@Valid BoardRecommentForm boardRecommentForm, BindingResult bindingResult,
                                @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "brecomment_form";
        }
        BoardRecomment boardRecomment = this.boardRecommentService.getBoareRecomment(id);
        if (!boardRecomment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.boardRecommentService.modify(boardRecomment, boardRecommentForm.getContent());
        return String.format("redirect:/board/%s#comment_%s", boardRecomment.getComment().getBoard().getId(), boardRecomment.getComment().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String recommentDelete(Principal principal, @PathVariable("id") Long id) {
        BoardRecomment boardRecomment = this.boardRecommentService.getBoareRecomment(id);
        if (!boardRecomment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.boardRecommentService.delete(boardRecomment);
        return String.format("redirect:/board/%s#comment_%s", boardRecomment.getComment().getBoard().getId(), boardRecomment.getComment().getId());
    }

}
