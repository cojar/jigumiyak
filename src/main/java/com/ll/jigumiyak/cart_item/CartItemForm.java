package com.ll.jigumiyak.cart_item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartItemForm {

    @NotEmpty(message = "수량을 선택해주세요")
    private String count;

}
