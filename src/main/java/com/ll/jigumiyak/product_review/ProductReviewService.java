package com.ll.jigumiyak.product_review;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.file.FileService;
import com.ll.jigumiyak.file.GenFile;
import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final FileService fileService;
    public ProductReview create(Product product, String content, int starRating, MultipartFile reviewImg, SiteUser siteUser) throws IOException {
        GenFile reviewImage = null;

        if (!reviewImg.isEmpty()) {
            reviewImage = fileService.upload(reviewImg, "product", "reviewImage", product.getName() + "_review");
        }

        ProductReview productReview = ProductReview.builder()
                .product(product)
                .content(content)
                .star_rating(starRating)
                .reviewImg(reviewImage)
                .reviewer(siteUser)
                .build();

        this.productReviewRepository.save(productReview);
        return productReview;
    }

    public Page<ProductReview> getList(Product product, int size, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        return this.productReviewRepository.findAllByProduct(product, pageable);
    }

    public ProductReview getReviewById(Long id) {
        Optional<ProductReview> review = productReviewRepository.findById(id);
        if (review.isPresent()){
            return review.get();
        } else {
            throw new DataNotFoundException("review not found");
        }
    }

    public void modify(ProductReview review, String content, MultipartFile reviewImage, int starRating) throws IOException {
        GenFile reviewImg = fileService.upload(reviewImage, "product", "reviewImage", review.getProduct().getName() + "_review");
        review = review.toBuilder()
                .content(content)
                .reviewImg(reviewImg)
                .star_rating(starRating)
                .build();

        this.productReviewRepository.save(review);
    }

    public void delete(ProductReview review) {
        this.productReviewRepository.delete(review);
    }

    public Double getAverageStarRatingForProduct(Long productId) {
        return productReviewRepository.getAverageStarRatingForProduct(productId);
    }
}
