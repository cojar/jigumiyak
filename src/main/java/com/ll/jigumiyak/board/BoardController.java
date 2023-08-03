package com.ll.jigumiyak.board;

import com.ll.jigumiyak.board_comment.BoardCommentForm;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("")
    public String boardList(Model model, @RequestParam(value="page", defaultValue="0") int page,
                            @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Board> paging = this.boardService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "board_list";
    }

    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable("id") Long id, BoardCommentForm boardCommentForm) {
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String boardCreate(BoardForm boardForm) {
        return "board_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String boardCreate(@Valid BoardForm boardForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "board_form";
        }
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());
        Board b = this.boardService.create(boardForm.getSubject(), boardForm.getContent(), siteUser);
        return String.format("redirect:/board/%d", b.getId());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String boardModify(BoardForm boardForm, @PathVariable("id") Long id, Principal principal) {
        Board board = this.boardService.getBoard(id);

        if(!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        boardForm.setSubject(board.getSubject());
        boardForm.setContent(board.getContent());
        return "board_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String boardModify(@Valid BoardForm boardForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "board_form";
        }
        Board board = this.boardService.getBoard(id);
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.boardService.modify(board, boardForm.getSubject(), boardForm.getContent());
        return String.format("redirect:/board/%d", id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String boardDelete(Principal principal, @PathVariable("id") Long id) {
        Board board = this.boardService.getBoard(id);
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.boardService.delete(board);
        return "redirect:/board";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String boardVote(Principal principal, @PathVariable("id") Long id) {
        Board board = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());
        this.boardService.vote(board, siteUser);
        return String.format("redirect:/board/%s", id);
    }
}
