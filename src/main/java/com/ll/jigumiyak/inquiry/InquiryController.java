package com.ll.jigumiyak.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/inquiry")
@RequiredArgsConstructor
@Controller
public class InquiryController {

    @GetMapping("")
    public String inquiry() {
        return "inquiry_main";
    }

    @GetMapping("/list")
    public String inquiryList() {
        return "inquiry_list";
    }
}
