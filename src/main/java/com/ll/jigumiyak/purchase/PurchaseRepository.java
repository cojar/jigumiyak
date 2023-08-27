package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByPurchaserAndPurchaseId(SiteUser purchaser, String purchaseId);
}
