package com.ll.jigumiyak.inquiry;

import com.ll.jigumiyak.inquiry_answer.InquiryAnswer;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public Inquiry create (String subject, String content, boolean email, String category, SiteUser inquirer) {
        Inquiry inquiry = Inquiry.builder()
                .subject(subject)
                .content(content)
                .email(email)
                .category(category)
                .inquirer(inquirer)
                .build();

        this.inquiryRepository.save(inquiry);

        return inquiry;
    }
}
