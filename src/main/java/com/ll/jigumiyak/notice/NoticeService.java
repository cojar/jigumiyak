package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.notice_category.NoticeCategory;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    public Page<Notice> getNoticeList(int page, int pageSize, String keywordCategory, String keyword, String order, String category) {
        Pageable pageable = PageRequest.of(page, pageSize);

        if (StringUtils.hasText(keyword) && StringUtils.hasText(keywordCategory) && StringUtils.hasText(category)) {
            return noticeRepository.searchByKeywordAndCategory(keyword, keywordCategory, category, pageable);
        } else if(StringUtils.hasText(keyword) && StringUtils.hasText(keywordCategory)){
            return noticeRepository.searchByKeyword(keyword, keywordCategory, pageable);
        } else if (StringUtils.hasText(category)) {
            return noticeRepository.searchByCategory(category, pageable);
        }
        return noticeRepository.findAll(pageable);
    }

    public Notice getNoticeById(Long id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()){
            return notice.get();
        } else {
            throw new DataNotFoundException("notice not found");
        }
    }

    public void create(NoticeCategory category, String title, String content, SiteUser siteUser) {
        Notice notice = new Notice();
        notice.setCategory(category);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setAuthor(siteUser);
        noticeRepository.save(notice);
    }
}
