package com.ll.jigumiyak.faq;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FaqService {

    private final FaqRepository faqRepository;

    public List<Faq> getList() {
        return this.faqRepository.findAll();
    }
    public Page<Faq> getList(int page, String  kw) {

        Pageable pageable = PageRequest.of(page, 10);

        return this.faqRepository.findAll(pageable);
    }

    public Faq create(String question, String answer, String category) {
        Faq faq = Faq.builder()
                .question(question)
                .answer(answer)
                .category(category)
                .build();

        this.faqRepository.save(faq);

        return faq;
    }

    public void modify(Faq faq, String question, String answer, String category) {
        faq = faq.toBuilder()
                .question(question)
                .answer(answer)
                .category(category)
                .build();

        this.faqRepository.save(faq);
    }

    public void delete(Faq faq) {
        this.faqRepository.delete(faq);
    }
}
