package com.ll.jigumiyak.admin;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board.BoardService;
import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.board_comment.BoardCommentService;
import com.ll.jigumiyak.board_recomment.BoardRecomment;
import com.ll.jigumiyak.board_recomment.BoardRecommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final BoardService boardService;
    private final BoardCommentService boardCommentService;
    private final BoardRecommentService boardRecommentService;

    @GetMapping("")
    public String admin() {
        return "admin/administration";
    }

    @GetMapping("/board")
    public String board(Model model, @RequestParam(value="page", defaultValue="0") int page,
                        @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Board> paging = this.boardService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "/admin/admin_board";
    }

    @GetMapping("/board/{id}")
    public String comment(Model model, @PathVariable("id") Long id, @RequestParam(value="page", defaultValue="0") int page,
                          @RequestParam(value = "cmtPage", defaultValue = "0") int cmtPage) {
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);

        Page<BoardComment> paging = this.boardCommentService.getList(board, 20,cmtPage);
        model.addAttribute("paging", paging);
        return "/admin/admin_board_comment";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/delete/{id}")
    public String boardDelete(@PathVariable("id") Long id) {
        Board board = this.boardService.getBoard(id);
        this.boardService.delete(board);
        return "redirect:/admin/board";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/comment/delete/{id}")
    public String commentDelete( @PathVariable("id") Long id) {
        BoardComment boardComment = this.boardCommentService.getBoardComment(id);
        this.boardCommentService.delete(boardComment);
        return String.format("redirect:/admin/board/%s", boardComment.getBoard().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/comment/{id}")
    public String recomment(Model model, @PathVariable("id") Long id) {
        BoardComment boardComment = this.boardCommentService.getBoardComment(id);
        model.addAttribute("comment", boardComment);

        List<BoardRecomment> recommentList = this.boardRecommentService.getList(boardComment);
        model.addAttribute("recommentList", recommentList);
        return "/admin/admin_board_recomment";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recomment/delete/{id}")
    public String recommentDelete( @PathVariable("id") Long id) {
        BoardRecomment boardRecomment = this.boardRecommentService.getBoareRecomment(id);
        this.boardRecommentService.delete(boardRecomment);
        return String.format("redirect:/admin/comment/%s", boardRecomment.getComment().getId());
    }
}
