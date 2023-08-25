package com.ll.jigumiyak.inquiry;

import com.ll.jigumiyak.inquiry_answer.InquiryAnswer;
import com.ll.jigumiyak.inquiry_answer.InquiryAnswerForm;
import com.ll.jigumiyak.inquiry_answer.InquiryAnswerService;
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
import java.util.List;

@RequestMapping("/inquiry")
@RequiredArgsConstructor
@Controller
public class InquiryController {

    private final InquiryService inquiryService;
    private final UserService userService;
    private final InquiryAnswerService inquiryAnswerService;

    @GetMapping("")
    public String inquiry() {
        return "inquiry/inquiry_main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String inquiryList(Model model, @RequestParam(value="page", defaultValue="0") int page, Principal principal) {
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());
        Page<Inquiry> paging = this.inquiryService.getList(page, siteUser);
        model.addAttribute("paging", paging);

        List<Inquiry> inquiryList = this.inquiryService.getList(siteUser);
        model.addAttribute("inquiryList", inquiryList);
        return "inquiry/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list/{id}")
    public String detail(Model model, @PathVariable("id") Long id, InquiryForm inquiryForm, InquiryAnswerForm inquiryAnswerForm) {
        Inquiry inquiry = this.inquiryService.getInquiry(id);
        model.addAttribute("inquiry", inquiry);

        List<InquiryAnswer> answerList = this.inquiryAnswerService.getList(inquiry);
        model.addAttribute("answerList", answerList);
        return "inquiry/inquiry_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String inquiryCreate (InquiryForm inquiryForm) {
        return "inquiry/inquiry_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String inquiryCreate (@Valid InquiryForm inquiryForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "inquiry/inquiry_form";
        }
        SiteUser inquirer = this.userService.getUserByLoginId(principal.getName());

        this.inquiryService.create(inquiryForm.getSubject(), inquiryForm.getContent(), inquiryForm.isEmail(), inquiryForm.getCategory(), inquirer, inquiryForm.getImgList());
        return "inquiry/inquiry_success";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String inquiryDelete( @PathVariable("id") Long id) {
        Inquiry inquiry = this.inquiryService.getInquiry(id);
        this.inquiryService.delete(inquiry);
        return "redirect:/admin/inquiry";
    }


}
