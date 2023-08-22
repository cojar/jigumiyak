package com.ll.jigumiyak.faq;

import com.ll.jigumiyak.DataNotFoundException;
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
public class FaqService {

    private final FaqRepository faqRepository;

    public List<Faq> getList() {
        return this.faqRepository.findAll();
    }
    public List<Faq> getList(String kw) {
        return this.faqRepository.findAllByKeywordInAllCategories(kw);
    }

    public List<Faq> getList(String category, String kw) {
            return this.faqRepository.findAllByCategoryAndKeyword(category, kw);
    }

    public Faq getFaq(Long id) {
        Optional<Faq> faq = this.faqRepository.findById(id);
        if (faq.isPresent()) {
            return faq.get();
        } else {
            throw new DataNotFoundException("faq not found");
        }
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
