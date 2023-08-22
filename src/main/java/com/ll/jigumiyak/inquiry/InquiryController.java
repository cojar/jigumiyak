package com.ll.jigumiyak.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/inquiry")
@RequiredArgsConstructor
@Controller
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("")
    public String inquiry() {
        return "inquiry/inquiry_main";
    }

}
