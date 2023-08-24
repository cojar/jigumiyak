package com.ll.jigumiyak.inquiry_answer;

import com.ll.jigumiyak.inquiry.Inquiry;
import com.ll.jigumiyak.inquiry.InquiryService;
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

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class InquiryAnswerController {

    private final InquiryAnswerService inquiryAnswerService;
    private final InquiryService inquiryService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id,
                               @Valid InquiryAnswerForm inquiryAnswerForm,
                               BindingResult bindingResult, Principal principal) {

        Inquiry inquiry = this.inquiryService.getInquiry(id);
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("inquiry", inquiry);
            return "admin/inquiry_detail";
        }
        this.inquiryAnswerService.create(inquiry, inquiryAnswerForm.getContent(), siteUser);
        this.inquiryService.updateState(inquiry, true);

        return String.format("redirect:/admin/inquiry/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify (Model model, InquiryAnswerForm inquiryAnswerForm, @PathVariable("id") Long id, Principal principal) {
        InquiryAnswer answer = this.inquiryAnswerService.getAnswer(id);
        model.addAttribute("answer", answer);
        if (!answer.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        inquiryAnswerForm.setContent(answer.getContent());
        return "#";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify (@Valid InquiryAnswerForm inquiryAnswerForm, BindingResult bindingResult,
                                @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return null;
        }
        InquiryAnswer answer = this.inquiryAnswerService.getAnswer(id);
        if (!answer.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.inquiryAnswerService.modify(answer, inquiryAnswerForm.getContent());
        return String.format("redirect:/admin/inquiry/%s", answer.getInquiry().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete (Principal principal, @PathVariable("id") Long id) {
        InquiryAnswer answer = this.inquiryAnswerService.getAnswer(id);
        if (!answer.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.inquiryAnswerService.delete(answer);
        return String.format("redirect:/admin/inquiry/%s", answer.getInquiry().getId());
    }
}
