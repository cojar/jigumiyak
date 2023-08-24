package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.purchase_detail.PurchaseDetail;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne
    private SiteUser purchaser;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.REMOVE)
    private List<PurchaseDetail> detailList;
}
// 주소
// 배송정보(준비 = 배송중 = 배송완료 = 구매확정)
// 오더디테일 -> 오더에 여러개
