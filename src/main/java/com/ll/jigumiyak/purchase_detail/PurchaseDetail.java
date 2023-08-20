package com.ll.jigumiyak.purchase_detail;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.purchase.Purchase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PurchaseDetail extends BaseEntity {

    @ManyToOne
    private Purchase purchase;
}

