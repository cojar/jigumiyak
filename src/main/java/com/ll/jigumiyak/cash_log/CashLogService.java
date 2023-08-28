package com.ll.jigumiyak.cash_log;

import com.ll.jigumiyak.purchase.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CashLogService {

    private final CashLogRepository cashLogRepository;

    public CashLog create(Purchase purchase) {

        if (!purchase.getPurchaseState().getState().equals("confirmed")) {
            return null;
        }

        CashLog cashLog = CashLog.builder()
                .totalAmount(purchase.getTotalAmount())
                .purchase(purchase)
                .build();

        this.cashLogRepository.save(cashLog);

        return cashLog;
    }

    public List<CashLog> getList() {
        return this.cashLogRepository.findAll();
    }
}
