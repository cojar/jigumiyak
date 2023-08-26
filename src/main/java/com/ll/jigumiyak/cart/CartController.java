package com.ll.jigumiyak.cart;

import com.ll.jigumiyak.cart_item.CartItem;
import com.ll.jigumiyak.cart_item.CartItemService;
import com.ll.jigumiyak.product.Product;
import com.ll.jigumiyak.product.ProductService;
import com.ll.jigumiyak.user.SiteUser;
import com.ll.jigumiyak.user.UserService;
import com.ll.jigumiyak.util.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequestMapping("/cart")
@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String myCart(Model model, Principal principal) {

        SiteUser owner = this.userService.getUserByLoginId(principal.getName());

        Cart cart = this.cartService.getCartByOwner(owner);

        List<CartItem> cartItemList = cart.getCartItemList();
        model.addAttribute("cartItemList", cartItemList);

        int totalAmount = 0;

        for (CartItem cartItem : cartItemList) {
            log.info(cartItem.getProduct().getName() + ": " + cartItem.getCount());
            totalAmount += cartItem.getCount() * cartItem.getProduct().getPrice();
        }

        log.info(String.format("totalAmount: %,d원", totalAmount));
        model.addAttribute("totalAmount", totalAmount);

        return "cart";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity addCartItem(@RequestParam("productId") Long productId,
                                      @RequestParam(value = "count", defaultValue = "-1") Integer count,
                                      Principal principal) {

        log.info("productId: " + productId);
        log.info("count: " + count);

        Product product = this.productService.getProduct(productId);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "해당 상품이 존재하지 않습니다", ""));
        }

        if (count < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-2", "수량을 하나 이상 선택해주세요", ""));
        }

        SiteUser owner = this.userService.getUserByLoginId(principal.getName());
        Cart cart = this.cartService.getCartByOwner(owner);
        CartItem cartItem = this.cartItemService.getCartItemByProductAndCart(product, cart);

        this.cartItemService.updateCount(count, cartItem);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "장바구니에 상품을 담았습니다.\n이동하시겠습니까?", "/cart"));
    }
}
