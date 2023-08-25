package com.ll.jigumiyak.product_review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductReviewForm {
    @NotEmpty(message = "내용은 필수항목입니다.")
    @Size(max = 200, message ="내용을 200자 이내로 작성해주세요." )
    private String content;
    @Max(value = 5, message = "별점은 5 이하여야 합니다.")
    @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
    private int star_rating;
    private MultipartFile reviewImage;
}
