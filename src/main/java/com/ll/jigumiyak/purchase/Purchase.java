package com.ll.jigumiyak.purchase;

import com.ll.jigumiyak.purchase_detail.PurchaseDetail;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.REMOVE)
    private List<PurchaseDetail> detailList;

    @ManyToOne
    private SiteUser purchaser;
}
// 주소
// 배송정보(준비 = 배송중 = 배송완료 = 구매확정)
// 오더디테일 -> 오더에 여러개
