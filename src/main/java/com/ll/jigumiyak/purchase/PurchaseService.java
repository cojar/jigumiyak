package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public Purchase getPurchaseByPurchaserAndPurchaseId(SiteUser purchaser, String purchaseId) {
        return this.purchaseRepository.findByPurchaserAndPurchaseId(purchaser, purchaseId)
                .orElse(null);
    }

    public void deletePurchase(Purchase purchase) {
        this.purchaseRepository.delete(purchase);
    }


}
