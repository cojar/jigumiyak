package com.ll.jigumiyak.notice_comment;

import com.ll.jigumiyak.notice.Notice;
import com.ll.jigumiyak.notice.NoticeService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class NoticeCommentController {
    private final NoticeService noticeService;
    private final NoticeCommentService noticeCommentService;
    private final UserService userService;

    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        Notice notice = this.noticeService.getNoticeById(id);
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("notice", notice);
            return "notice_comment";
        }
        NoticeComment comment = this.noticeCommentService.create(notice, commentForm.getContent(), siteUser);
        return String.format("redirect:/notice/detail/%s#comment_%s", comment.getNotice().getId(), notice.getId());
    }

    @GetMapping("/modify/{id}")
    public String commentModify(CommentForm commentForm, @PathVariable("id") Long id, Principal principal) {
        NoticeComment comment = this.noticeCommentService.getCommentById(id);
        if (!comment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "comment_form";
    }

    @PostMapping("/modify/{id}")
    public String answerModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                               @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        NoticeComment comment = this.noticeCommentService.getCommentById(id);
        if (!comment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.noticeCommentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/notice/detail/%s#comment_%s",
                comment.getNotice().getId(), comment.getId());
    }

    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id) {
        NoticeComment comment = this.noticeCommentService.getCommentById(id);
        if (!comment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.noticeCommentService.delete(comment);
        return String.format("redirect:/notice/detail/%s", comment.getNotice().getId());
    }
}
