package com.ll.jigumiyak.product_review;

import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.product.ProductService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;

@RequestMapping("/review")
@RequiredArgsConstructor
@Controller
public class ProductReviewController {
    private final ProductService productService;
    private final UserService userService;
    private final ProductReviewService productReviewService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Long id,
                                @Valid ProductReviewForm productReviewForm,
                                BindingResult bindingResult, Principal principal) throws IOException {

        Product product = this.productService.getProduct(id);
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            return "product_detail";
        }
        ProductReview review = this.productReviewService.create(product, productReviewForm.getContent(), productReviewForm.getStar_rating(), productReviewForm.getReviewImage(), siteUser);
        return String.format("redirect:/product/%s#comment_%s", review.getProduct().getId(), review.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(Model model, ProductReviewForm productReviewForm, @PathVariable("id") Long id, Principal principal) {
        ProductReview review = this.productReviewService.getReviewById(id);
        model.addAttribute("review", review);
        if (!review.getReviewer().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        System.out.println(review.getProduct().getName());
        productReviewForm.setContent(review.getContent());
        return "product_review_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid ProductReviewForm productReviewForm, BindingResult bindingResult,
                               @PathVariable("id") Long id, Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            return "product_review_form";
        }
        ProductReview review = this.productReviewService.getReviewById(id);
        if (!review.getReviewer().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.productReviewService.modify(review, productReviewForm.getContent(), productReviewForm.getReviewImage(), productReviewForm.getStar_rating());
        return String.format("redirect:/product/%s#review_%s",
                review.getProduct().getId(), review.getId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id) {
        ProductReview review = this.productReviewService.getReviewById(id);
        if (!review.getReviewer().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.productReviewService.delete(review);
        return String.format("redirect:/product/%s", review.getProduct().getId());
    }
}
