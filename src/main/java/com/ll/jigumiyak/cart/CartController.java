package com.ll.jigumiyak.cart;

import com.ll.jigumiyak.cart_item.CartItem;
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
import org.springframework.ui.Model;
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
    @GetMapping("/cart")
    public String myCart(Principal principal, Model model){
        SiteUser user = userService.getUserByLoginId(principal.getName());
        List<CartItem> cartItemList = cartService.getCartList(user.getId());
        // good
        for (CartItem cartItem : cartItemList){
            System.out.println(cartItem.getProduct().getName());
        }
        model.addAttribute("cartItemList", cartItemList);
        return "cart";
    }

    @PostMapping("/cart/{id}")
    String addCart(@Valid CartItemForm cartItemForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Long id){
        if(bindingResult.hasErrors()){
            return "product_list";
        }

        Product product = productService.getProduct(id);
        SiteUser siteUser = userService.getUserByLoginId(principal.getName());
        System.out.println(principal.getName());
        cartService.addCart(cartItemForm, siteUser, product);
        return "redirect:/";
    }
}
