package com.ll.jigumiyak.product;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.file.FileService;
import com.ll.jigumiyak.file.GenFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;

    public Product create(String name, String description, int price, MultipartFile file) throws IOException {
        GenFile thumbnailImg = this.fileService.upload(file, "product", "thumbnailImage", name);

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .thumbnailImg(thumbnailImg)
                .hit(0L)
                .build();
        this.productRepository.save(product);
        return product;
    }

    public Page<Product> getList(int page, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
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
