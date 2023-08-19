package com.ll.jigumiyak.product;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.file.FileService;
import com.ll.jigumiyak.file.GenFile;
import com.ll.jigumiyak.nutrient.Nutrient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FileService fileService;

    public Product create(String name, String description, int price, int quantity, MultipartFile file, List<Nutrient> nutrientList) throws IOException {

        GenFile thumbnailImg = this.fileService.upload(file, "product", "thumbnailImage", name);

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
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
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new DataNotFoundException("product not found");
        }
    }
}
