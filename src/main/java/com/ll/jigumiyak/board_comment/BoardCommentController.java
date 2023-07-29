package com.ll.jigumiyak.board_comment;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class BoardCommentController {
    private final BoardService boardService;
    private final BoardCommentService boardCommentService;

    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id, @Valid BoardCommentForm boardCommentForm, BindingResult bindingResult) {
        Board board = this.boardService.getBoard(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("board", board);
            return "board_detail";
        }
        this.boardCommentService.create(board, boardCommentForm.getContent());
        return String.format("redirect:/board/detail/%d", id);
    }
}
