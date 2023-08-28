package com.ll.jigumiyak.cash_log;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.purchase.Purchase;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CashLog extends BaseEntity {

    private Integer totalAmount;

    @OneToOne
    private Purchase purchase;
}
