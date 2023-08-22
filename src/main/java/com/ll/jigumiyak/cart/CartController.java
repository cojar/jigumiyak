package com.ll.jigumiyak.cart;

import com.ll.jigumiyak.cart_item.CartItemForm;
import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.product.ProductService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;
    @GetMapping("/cart/{id}")
    public String myCart(){
        return "cart";
    }

    @PostMapping("/cart/{id}")
    @ResponseBody
    Object addCart(@Valid CartItemForm cartItemForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Long id){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        Product product = productService.getProduct(id);
        SiteUser siteUser = userService.getUserByLoginId(principal.getName());
        Long cartItemId = cartService.addCart(cartItemForm, siteUser, product);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
}
