package com.ll.jigumiyak.inquiry;

import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/inquiry")
@RequiredArgsConstructor
@Controller
public class InquiryController {

    private final InquiryService inquiryService;
    private final UserService userService;

    @GetMapping("")
    public String inquiry() {
        return "inquiry/inquiry_main";
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

        this.inquiryService.create(inquiryForm.getSubject(), inquiryForm.getContent(), inquiryForm.isEmail(), inquiryForm.getCategory(), inquirer);
        return "inquiry/inquiry_success";
    }
}
