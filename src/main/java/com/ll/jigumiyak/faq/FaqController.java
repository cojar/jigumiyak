package com.ll.jigumiyak.faq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/faq")
@RequiredArgsConstructor
@Controller
public class FaqController {

    private final FaqService faqService;

    @GetMapping("")
    public String faqList(Model model, @RequestParam(value = "category", defaultValue = "") String category,
                          @RequestParam(value = "kw", defaultValue = "") String kw){
        if (category.equals("")) {
            List<Faq> faqList = this.faqService.getList(kw);
            model.addAttribute("faqList", faqList);
        } else {
            List<Faq> faqList = this.faqService.getList(category, kw);
            model.addAttribute("faqList", faqList);
        }

        return "faq/faq";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String faqCreate(FaqForm faqForm) {
        return "admin/faq_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String faqCreate(@Valid FaqForm faqForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/faq_form";
        }
        this.faqService.create(faqForm.getQuestion(), faqForm.getAnswer(), faqForm.getCategory());
        return "redirect:/admin/faq";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String faqModify(FaqForm faqForm, @PathVariable("id") Long id) {
        Faq faq = this.faqService.getFaq(id);

        faqForm.setQuestion(faq.getQuestion());
        faqForm.setAnswer(faq.getAnswer());
        faqForm.setCategory(faq.getCategory());
        return "admin/faq_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String faqModify(@Valid FaqForm faqForm, BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "admin/faq_form";
        }
        Faq faq = this.faqService.getFaq(id);

        this.faqService.modify(faq, faqForm.getQuestion(), faqForm.getAnswer(), faqForm.getCategory());
        return "redirect:/admin/faq";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String boardDelete(@PathVariable("id") Long id) {
        Faq faq = this.faqService.getFaq(id);
        this.faqService.delete(faq);
        return "redirect:/admin/faq";
    }

}
