package com.ll.jigumiyak.board_comment;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board.BoardService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/bcomment")
@RequiredArgsConstructor
@Controller
public class BoardCommentController {

    private final BoardService boardService;
    private final BoardCommentService boardCommentService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid BoardCommentForm boardCommentForm,
                                BindingResult bindingResult, Principal principal) {

        Board board = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("board", board);
            return "board_detail";
        }
        BoardComment comment = this.boardCommentService.create(board, boardCommentForm.getContent(), siteUser);
        return String.format("redirect:/board/%s#comment_%s", comment.getBoard().getId(), comment.getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(Model model, BoardCommentForm boardCommentForm, @PathVariable("id") Long id, Principal principal) {
        BoardComment boardComment = this.boardCommentService.getBoardComment(id);
        model.addAttribute("boardComment", boardComment);

        if (!boardComment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        boardCommentForm.setContent(boardComment.getContent());
        return "bcomment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify(@Valid BoardCommentForm boardCommentForm, BindingResult bindingResult,
                                @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "bcomment_form";
        }
        BoardComment boardComment = this.boardCommentService.getBoardComment(id);
        if (!boardComment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.boardCommentService.modify(boardComment, boardCommentForm.getContent());
        return String.format("redirect:/board/%s#comment_%s", boardComment.getBoard().getId(), boardComment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id) {
        BoardComment boardComment = this.boardCommentService.getBoardComment(id);
        if (!boardComment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.boardCommentService.delete(boardComment);
        return String.format("redirect:/board/%s", boardComment.getBoard().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String boardCommentVote(Model model, Principal principal, @PathVariable("id") Long id) {
        BoardComment boardComment = this.boardCommentService.getBoardComment(id);
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());

        if (boardComment.getAuthor().getLoginId().equals(siteUser.getLoginId())) {
            throw new RuntimeException("본인이 작성한 글은 추천할 수 없습니다.");
        }

        this.boardCommentService.vote(boardComment, siteUser);
        model.addAttribute("comment", boardComment);
        return String.format("redirect:/board/%s#comment_%s :: #board_detail", boardComment.getBoard().getId(), boardComment.getId());
    }
}