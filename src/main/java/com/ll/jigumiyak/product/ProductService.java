package com.ll.jigumiyak.product;

import com.ll.jigumiyak.file.FileService;
import com.ll.jigumiyak.file.GenFile;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;

    public Product create(String name, String description, int price, int quantity, Long inventory, MultipartFile file, List<Nutrient> nutrientList) throws IOException {

        GenFile thumbnailImg = this.fileService.upload(file, "product", "thumbnailImage", name);

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .inventory(inventory)
                .thumbnailImg(thumbnailImg)
                .nutrientList(nutrientList)
                .hit(0L)
                .build();

        this.productRepository.save(product);

        return product;
    }

    public Page<Product> getList(int page, int pageSize, String keyword, String category) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        if (StringUtils.hasText(keyword) && StringUtils.hasText(category)) {
            return productRepository.findByCategoryNameAndKeyword(keyword, category, pageable);
        } else if (StringUtils.hasText(category)) {
            return productRepository.findByNutrientCategoryName(category, pageable);
        }
        return this.productRepository.findAllByKeyword(keyword, pageable);
    }


    public Product getProduct(Long id) {

        return this.productRepository.findById(id)
                .orElse(null);
    }

    public Product createDetailImg(Product product, MultipartFile detailImg) throws IOException {
        GenFile detailImage = fileService.upload(detailImg, "product", "detailImage", product.getName() + "_detail");

        product = product.toBuilder()
                .detailImg(detailImage)
                .build();
        productRepository.save(product);
        return product;
    }

    public void vote(Product product, SiteUser siteUser) {
        if (product.getVoter().contains(siteUser)) {
            product.getVoter().remove(siteUser);
        } else {
            product.getVoter().add(siteUser);
        }
        this.productRepository.save(product);
    }

    public List<Product> findProductsByNutrientName(String nutrientName) {
        return productRepository.findByNutrientListName(nutrientName);
    }

    public List<Product> getList() {
        return productRepository.findAll();
    }
}
