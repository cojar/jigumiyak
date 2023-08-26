package com.ll.jigumiyak.inquiry_answer;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.inquiry.Inquiry;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InquiryAnswerService {

    private final InquiryAnswerRepository inquiryAnswerRepository;

    public InquiryAnswer create(Inquiry inquiry, String content, SiteUser siteUser) {

        InquiryAnswer answer = InquiryAnswer.builder()
                .content(content)
                .author(siteUser)
                .inquiry(inquiry)
                .build();
        this.inquiryAnswerRepository.save(answer);

        return answer;
    }

    public InquiryAnswer getAnswer(Long id) {
        Optional<InquiryAnswer> answer = this.inquiryAnswerRepository.findById(id);
        if (answer.isPresent()) {
            return  answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify (InquiryAnswer answer, String content) {

        answer = answer.toBuilder()
                .content(content)
                .build();
        this.inquiryAnswerRepository.save(answer);
    }

    public void delete (InquiryAnswer answer) {
        this.inquiryAnswerRepository.delete(answer);
    }

    public List<InquiryAnswer> getList (Inquiry inquiry) {
        return this.inquiryAnswerRepository.findAllByInquiry(inquiry);
    }


}
