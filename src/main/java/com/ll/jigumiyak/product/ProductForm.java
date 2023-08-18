package com.ll.jigumiyak.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductForm {
    @NotEmpty
    @Size(max=50)
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private Integer price;
    @NotEmpty
    private Integer quantity;
    @NotEmpty
    private MultipartFile thumbnailImage;
    @NotEmpty
    private List<String> nutrientList;
}
