package com.ll.jigumiyak.notice_comment;

import com.ll.jigumiyak.notice.Notice;
import com.ll.jigumiyak.notice.NoticeService;
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

@RequiredArgsConstructor
@Controller
@RequestMapping("/ncomment")
public class NoticeCommentController {
    private final NoticeService noticeService;
    private final NoticeCommentService noticeCommentService;
    private final UserService userService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid NoticeCommentForm noticeCommentForm, BindingResult bindingResult, Principal principal) {
        Notice notice = this.noticeService.getNoticeById(id);
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("notice", notice);
            return "notice_detail";
        }
        NoticeComment comment = this.noticeCommentService.create(notice, noticeCommentForm.getContent(), siteUser);
        return String.format("redirect:/notice/%s#comment_%s", comment.getNotice().getId(), notice.getId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(Model model, NoticeCommentForm noticeCommentForm, @PathVariable("id") Long id, Principal principal) {
        NoticeComment comment = this.noticeCommentService.getCommentById(id);
        model.addAttribute("comment", comment);
        if (!comment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        noticeCommentForm.setContent(comment.getContent());
        return "ncomment_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid NoticeCommentForm noticeCommentForm, BindingResult bindingResult,
                               @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "ncomment_form";
        }
        NoticeComment comment = this.noticeCommentService.getCommentById(id);
        if (!comment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.noticeCommentService.modify(comment, noticeCommentForm.getContent());
        return String.format("redirect:/notice/%s#comment_%s",
                comment.getNotice().getId(), comment.getId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id) {
        NoticeComment comment = this.noticeCommentService.getCommentById(id);
        if (!comment.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.noticeCommentService.delete(comment);
        return String.format("redirect:/notice/%s", comment.getNotice().getId());
    }
}
