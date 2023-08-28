package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.address.Address;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public Purchase create(String purchaseId,
                           String purchaseName,
                           String purchaserName,
                           String purchaserPhoneNumber,
                           String receiverName,
                           String receiverPhoneNumber,
                           Address receiverAddress,
                           String deliveryRequest,
                           int totalAmount,
                           SiteUser purchaser) {

        Purchase purchase = Purchase.builder()
                .purchaseState(PurchaseState.BEFORE)
                .purchaseId(purchaseId)
                .purchaseName(purchaseName)
                .purchaserName(purchaserName)
                .purchaserPhoneNumber(purchaserPhoneNumber)
                .receiverName(receiverName)
                .receiverPhoneNumber(receiverPhoneNumber)
                .receiverAddress(receiverAddress)
                .deliveryRequest(deliveryRequest)
                .totalAmount(totalAmount)
                .purchaser(purchaser)
                .build();

        this.purchaseRepository.save(purchase);

        return purchase;
    }

    public Purchase getPurchase(Long id) {
        return this.purchaseRepository.findById(id)
                .orElse(null);
    }

    public List<Purchase> getList() {
        return this.purchaseRepository.findAll();
    }

    public Purchase getPurchaseByPurchaserAndPurchaseId(SiteUser purchaser, String purchaseId) {
        return this.purchaseRepository.findByPurchaserAndPurchaseId(purchaser, purchaseId)
                .orElse(null);
    }

    public void delete(Purchase purchase) {
        this.purchaseRepository.delete(purchase);
    }

    public void updateSuccess(Purchase purchase,
                              String paymentKey,
                              String method,
                              LocalDateTime payDate,
                              Integer suppliedAmount,
                              Integer vat,
                              String paymentDetail) {

        purchase = purchase.toBuilder()
                .purchaseState(PurchaseState.PAID)
                .paymentKey(paymentKey)
                .method(method)
                .payDate(payDate)
                .suppliedAmount(suppliedAmount)
                .vat(vat)
                .paymentDetail(paymentDetail)
                .build();

        this.purchaseRepository.save(purchase);
    }

    public List<Purchase> getListByPurchaser(SiteUser purchaser) {
        return this.purchaseRepository.findByPurchaser(purchaser);
    }
}
