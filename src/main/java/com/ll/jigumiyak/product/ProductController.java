package com.ll.jigumiyak.product;

import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientService;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryService;
import com.ll.jigumiyak.nutrient_caution.NutrientCautionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final NutrientService nutrientService;
    private final NutrientCategoryService nutrientCategoryService;
    private final NutrientCautionService nutrientCautionService;
    @GetMapping("")
    public String productList(Model model,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "pageSize", defaultValue = "8") int pageSize,
                              @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        Page<Product> noticePaging = productService.getList(page, pageSize, keyword);
        model.addAttribute("paging", noticePaging);
        return "product_list";
    }
    @GetMapping("/{id}")
    public String detail(Model model,
                         @PathVariable("id") Long id) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);

        List<Nutrient> nutrientList  = nutrientService.getList();
        model.addAttribute("nutrientList", nutrientList);
        return "product_detail";
    }
    @GetMapping("/create")
    public String create(Model model, ProductForm productForm){
        List<Nutrient> nutrientList = nutrientService.getList();
        model.addAttribute("nutrientList", nutrientList);
        // 등록할때 키워드 검색이나 자동완성이런걸로 보완 예정
        return "product_form";
    }
}
