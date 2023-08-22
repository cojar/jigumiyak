package com.ll.jigumiyak.faq;

import com.ll.jigumiyak.inquiry.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    Page<Faq> findAll(Pageable pageable);
}
