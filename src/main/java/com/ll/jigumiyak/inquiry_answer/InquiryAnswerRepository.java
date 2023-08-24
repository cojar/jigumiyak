package com.ll.jigumiyak.inquiry_answer;

import com.ll.jigumiyak.inquiry.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Long> {
    List<InquiryAnswer> findAllByInquiry(Inquiry inquiry);
}
