package com.ll.jigumiyak.board;

import com.ll.jigumiyak.board_comment.BoardCommentForm;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/list")
    public String boardList(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Board> paging = this.boardService.getList(page);
        model.addAttribute("paging", paging);
        return "board_list";
    }

    @GetMapping("/detail/{id}")
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
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.boardService.create(boardForm.getSubject(), boardForm.getContent(), siteUser);
        return "redirect:/board/list";
    }
}
