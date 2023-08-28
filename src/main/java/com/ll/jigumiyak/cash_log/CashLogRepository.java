package com.ll.jigumiyak.cash_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
