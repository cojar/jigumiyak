package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.notice_category.NoticeCategory;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    public Page<Notice> getNoticeList(int page, int pageSize, String keywordCategory, String category, String keyword, String order) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        if (StringUtils.hasText(keyword) && StringUtils.hasText(keywordCategory) && StringUtils.hasText(category)) {
            return noticeRepository.searchByKeywordAndCategory(keyword, keywordCategory, category, pageable);
        } else if(StringUtils.hasText(keyword) && StringUtils.hasText(keywordCategory)){
            return noticeRepository.searchByKeyword(keyword, keywordCategory, pageable);
        } else if (StringUtils.hasText(category)) {
            return noticeRepository.searchByCategory(category, pageable);
        }
        return noticeRepository.findAll(pageable);
    }

    public Notice getNotice(Long id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()){
            return notice.get();
        } else {
            throw new DataNotFoundException("notice not found");
        }
    }

    public Notice create(NoticeCategory category, String title, String content, SiteUser siteUser) {
        Notice notice = new Notice();
        notice.setCategory(category);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setCreateDate(LocalDateTime.now());
        notice.setAuthor(siteUser);
        noticeRepository.save(notice);
        return notice;
    }

    public void delete(Notice notice) {
        this.noticeRepository.delete(notice);
    }

    public Notice modify(Notice notice, String title, String content, NoticeCategory category) {
        notice.setTitle(title);
        notice.setContent(content);
        notice.setModifyDate(LocalDateTime.now());
        notice.setCategory(category);
        this.noticeRepository.save(notice);
        return notice;
    }
}
