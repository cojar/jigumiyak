package com.ll.jigumiyak.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    public Page<Notice> getNoticeList(int page, int pageSize, String keywordCategory, String keyword, String order, String category) {
        Pageable pageable = PageRequest.of(page, pageSize);

        if (StringUtils.hasText(keyword) && StringUtils.hasText(keywordCategory) && StringUtils.hasText(category)) {
            return noticeRepository.searchByKeywordAndCategory(keyword, keywordCategory, category, pageable);
        } else if(StringUtils.hasText(keyword) && StringUtils.hasText(keywordCategory)){
            return noticeRepository.searchByKeyword(keyword, keywordCategory,null, pageable);
        } else if (StringUtils.hasText(category)) {
            return noticeRepository.searchByCategory(null, null, category, pageable);
        }
        return noticeRepository.findAll(pageable);
    }
}
