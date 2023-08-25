package com.ll.jigumiyak.inquiry;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.inquiryImg.InquiryImg;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public Inquiry create (String subject, String content, boolean email, String category, SiteUser inquirer, List<InquiryImg> imgList) {
        Inquiry inquiry = Inquiry.builder()
                .subject(subject)
                .content(content)
                .email(email)
                .category(category)
                .inquirer(inquirer)
                .imgList(imgList)
                .build();

        this.inquiryRepository.save(inquiry);

        return inquiry;
    }

    public Page<Inquiry> getList(int page, SiteUser siteUser) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.inquiryRepository.findAllByInquirer(siteUser, pageable);
    }

    public List<Inquiry> getList(SiteUser siteUser) {
        return this.inquiryRepository.findAllByInquirer(siteUser);
    }

    public Page<Inquiry> getList(int page, boolean state) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
        return this.inquiryRepository.findAllByState(state, pageable);
    }

    public Inquiry getInquiry(Long id) {
        Optional<Inquiry> inquiry = this.inquiryRepository.findById(id);
        if (inquiry.isPresent()) {
            return inquiry.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }

    public void delete(Inquiry inquiry) {
        this.inquiryRepository.delete(inquiry);
    }

    public void updateState (Inquiry inquiry, boolean state) {
        inquiry = inquiry.toBuilder()
                .state(state)
                .build();
        this.inquiryRepository.save(inquiry);
    }
}
