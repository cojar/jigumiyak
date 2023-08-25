package com.ll.jigumiyak.product;

import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.nutrient.Nutrient;
import com.ll.jigumiyak.nutrient.NutrientService;
import com.ll.jigumiyak.nutrient_category.NutrientCategory;
import com.ll.jigumiyak.nutrient_category.NutrientCategoryService;
import com.ll.jigumiyak.nutrient_caution.NutrientCautionService;
import com.ll.jigumiyak.product_review.ProductReview;
import com.ll.jigumiyak.product_review.ProductReviewForm;
import com.ll.jigumiyak.product_review.ProductReviewService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final NutrientService nutrientService;
    private final NutrientCategoryService nutrientCategoryService;
    private final UserService userService;
    private final ProductReviewService productReviewService;

    @GetMapping("")
    public String productList(Model model,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "pageSize", defaultValue = "8") int pageSize,
                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
                              @RequestParam(value = "category", defaultValue = "") String category) {
        Page<Product> productPaging = productService.getList(page, pageSize, keyword, category);
        model.addAttribute("paging", productPaging);

        List<NutrientCategory> categoryList = nutrientCategoryService.getList();
        model.addAttribute("categoryList", categoryList);

        // 검색 값 유지를 위한 modeling
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "product_list";
    }

    @GetMapping("/{id}")
    public String detail(Model model, ProductReviewForm productReviewForm,
                         @PathVariable("id") Long id,
                         @RequestParam(value = "cmtPage", defaultValue = "0") int cmtPage) {

        Product product = productService.getProduct(id);
        model.addAttribute("product", product);

        Page<ProductReview> paging = this.productReviewService.getList(product, 5,cmtPage);
        model.addAttribute("paging", paging);

        Double productAvg = productReviewService.getAverageStarRatingForProduct(id);
        model.addAttribute("productAvg", productAvg);
        return "product_detail";
    }

    @GetMapping("/create")
    public String create(Model model, ProductForm productForm) {
        List<Nutrient> nutrientList = nutrientService.getList();
        model.addAttribute("nutrientList", nutrientList);
        // 등록할때 키워드 검색이나 자동완성이런걸로 보완 예정
        return "product_form";
    }

    @PostMapping("/create")
    public String create(@Valid ProductForm productForm, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "product_form";
        }
        List<Nutrient> nutrientList = new ArrayList<>();
        for (String name : productForm.getNutrientList()) {
            System.out.println(name);
            nutrientList.add(nutrientService.findByName(name));
        }
        productService.create(productForm.getName(), productForm.getDescription(), Integer.parseInt(productForm.getPrice()), Integer.parseInt(productForm.getQuantity()), Long.valueOf(productForm.getInventory()), productForm.getThumbnailImage(), nutrientList);
        return "redirect:/product";
    }

    @GetMapping("/create/{id}")
    public String createDetail(Model model, ProductDetailForm productDetailForm,
                               @PathVariable("id") Long id){
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "product_detail_form";
    }

    @PostMapping("/create/{id}")
    public String createDetail(@PathVariable("id") Long id,
                               @Valid ProductDetailForm productDetailForm,
                               BindingResult bindingResult, Model model) throws IOException {
        if(bindingResult.hasErrors()){
            return "product_detail_form";
        }
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        productService.createDetailImg(product, productDetailForm.getDetailImg());
        return String.format("redirect:/product/%s", product.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String boardVote(Model model, Principal principal, @PathVariable("id") Long id) {
        Product product = this.productService.getProduct(id);
        SiteUser siteUser = this.userService.getUserByLoginId(principal.getName());

        this.productService.vote(product, siteUser);

        model.addAttribute("product", product);
        return "product_detail :: #product_detail";
    }


    @ResponseBody
    @RequestMapping(value = "/searchNutrient_ajax", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public String wordSearchShow(HttpServletRequest request) {

        String searchWord = request.getParameter("searchWord");

        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("searchWord", searchWord);

        List<String> nutrientList = nutrientService.containingNutrientName(paraMap);
        JSONArray jsonArr = new JSONArray();
        if (nutrientList != null) {
            for (String word : nutrientList) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("word", word);
                jsonArr.add(jsonObj);
            }
        }
        return jsonArr.toString();
    }
}
