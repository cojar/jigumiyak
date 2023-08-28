package com.ll.jigumiyak.purchase;

import lombok.Getter;

@Getter
public enum PurchaseState {

    BEFORE("before", "진행중"),
    PAID("paid", "결제완료"),
    CANCEL("cancel", "결제취소"),
    REFUND("refund", "환불"),
    DELIVERY_READY("delivery_ready", "배송준비"),
    DELIVERY_ONGOING("delivery_ongoing", "배송중"),
    DELIVERY_DONE("delivery_done", "배송완료"),
    CONFIRMED("confirmed", "구매확정");

    private String state;
    private String stateKor;

    PurchaseState(String state, String stateKor) {
        this.state = state;
        this.stateKor = stateKor;
    }
}
