package com.ll.jigumiyak.notice_category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NoticeCategoryService {
    private final NoticeCategoryRepository noticeCategoryRepository;
    public NoticeCategory getCategoryByName(String category){
        return noticeCategoryRepository.findByName(category);
    }
}
