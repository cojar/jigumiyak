package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.address.Address;
import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.purchase_item.PurchaseItem;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Purchase extends BaseEntity {

    private PurchaseState purchaseState;

    private String purchaseId;

    private String purchaseName;

    private String purchaserName;

    private String purchaserPhoneNumber;

    private String receiverName;

    private String receiverPhoneNumber;

    @OneToOne
    private Address receiverAddress;

    private String deliveryRequest;

    private Integer totalAmount;

    private Integer suppliedAmount;

    private Integer vat;

    private String paymentKey;

    private String method;

    private LocalDateTime payDate;

    private String paymentDetail;

    @ManyToOne
    private SiteUser purchaser;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.REMOVE)
    private List<PurchaseItem> purchaseItemList;
}
