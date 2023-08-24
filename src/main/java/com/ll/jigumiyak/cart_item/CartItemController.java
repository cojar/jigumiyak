package com.ll.jigumiyak.cart_item;

import com.ll.jigumiyak.util.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/cartItem")
@RequiredArgsConstructor
@Controller
public class CartItemController {

    private final CartItemService cartItemService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/increase")
    @ResponseBody
    public ResponseEntity increaseCount(@RequestParam("cartItemId") Long cartItemId) {

        CartItem cartItem = this.cartItemService.getCartItem(cartItemId);

        if (cartItem == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "해당 아이템이 존재하지 않습니다", ""));
        }

        this.cartItemService.updateCount(1, cartItem);

        Map<String, Object> cartItemAttributes = new HashMap<>();
        cartItemAttributes.put("count", cartItem.getCount());
        cartItemAttributes.put("price", String.format("%,d원", cartItem.getCount() * cartItem.getProduct().getPrice()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "해당 아이템의 개수를 증가시켰습니다", cartItemAttributes));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/decrease")
    @ResponseBody
    public ResponseEntity decreaseCount(@RequestParam("cartItemId") Long cartItemId) {

        CartItem cartItem = this.cartItemService.getCartItem(cartItemId);

        if (cartItem == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-1", "해당 아이템이 존재하지 않습니다", ""));
        }

        if (cartItem.getCount() == 1) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("F-2", "아이템은 최소 1개 이상이어야 합니다", ""));
        }

        this.cartItemService.updateCount(-1, cartItem);

        Map<String, Object> cartItemAttributes = new HashMap<>();
        cartItemAttributes.put("count", cartItem.getCount());
        cartItemAttributes.put("price", String.format("%,d원", cartItem.getCount() * cartItem.getProduct().getPrice()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new RsData<>("S-1", "해당 아이템의 개수를 감소시켰습니다", cartItemAttributes));
    }
}