package com.ll.jigumiyak.faq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/faq")
@RequiredArgsConstructor
@Controller
public class FaqController {

    private final FaqService faqService;

    @GetMapping("")
    public String faq(Model model, @RequestParam(value = "category", defaultValue = "order") String category){
        Faq faq;
        return "faq/faq";
    }

    @GetMapping("/create")
    public String faqCreate(FaqForm faqForm) {
        return "admin/faq_form";
    }

    @PostMapping("/create")
    public String faqCreate(@Valid FaqForm faqForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/faq_form";
        }
        this.faqService.create(faqForm.getQuestion(), faqForm.getAnswer(), faqForm.getCategory());
        return "redirect:/admin/faq";
    }

}
