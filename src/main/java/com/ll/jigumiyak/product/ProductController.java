package com.ll.jigumiyak.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/product")
@RequiredArgsConstructor
@Controller
public class ProductController {
    @GetMapping("")
    public String productList() {
        return "product_list";
    }
}
