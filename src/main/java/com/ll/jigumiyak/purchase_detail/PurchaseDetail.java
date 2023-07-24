package com.ll.jigumiyak.purchase_detail;

import com.ll.jigumiyak.purchase.Purchase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Purchase purchase;
}

