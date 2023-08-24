package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByPurchaserAndPurchaseId(SiteUser purchaser, String purchaseId);
}
