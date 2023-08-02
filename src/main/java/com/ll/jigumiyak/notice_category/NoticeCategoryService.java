package com.ll.jigumiyak.notice_category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeCategoryService {
    private final NoticeCategoryRepository noticeCategoryRepository;
    public NoticeCategory getCategoryByName(String name){
        Optional<NoticeCategory> noticeCategory = noticeCategoryRepository.findByName(name);
        if(noticeCategory.isPresent()){
            return noticeCategory.get();
        } else {
            throw new DataNotFoundException("noticeCategory not found");
        }
    }
    public List<NoticeCategory> getNoticeCategoryList(){
        return noticeCategoryRepository.findAll();
    }
}
