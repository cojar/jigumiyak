package com.ll.jigumiyak.cart_item;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemForm {
    @Min(message = "수량을 하나 이상 선택해주세요", value = 1)
    private Integer count;
}
