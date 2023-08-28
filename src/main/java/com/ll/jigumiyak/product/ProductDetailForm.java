package com.ll.jigumiyak.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductDetailForm {
    private MultipartFile detailImg;
}
