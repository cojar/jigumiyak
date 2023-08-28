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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public Page<Notice> getNoticeList(int page, int pageSize, String keywordCategory, String category, String keyword, String order) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        if (StringUtils.hasText(keyword) && StringUtils.hasText(keywordCategory) && StringUtils.hasText(category)) {
            return noticeRepository.searchByKeywordAndCategory(keyword, keywordCategory, category, pageable);
        } else if (StringUtils.hasText(keyword) && StringUtils.hasText(keywordCategory)) {
            return noticeRepository.searchByKeyword(keyword, keywordCategory, pageable);
        } else if (StringUtils.hasText(category)) {
            return noticeRepository.searchByCategory(category, pageable);
        }
        return noticeRepository.findAll(pageable);
    }

    public Notice getNotice(Long id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new DataNotFoundException("notice not found");
        }
    }

    public Notice create(NoticeCategory category, String title, String content, SiteUser author) {

        Notice notice = Notice.builder()
                .category(category)
                .title(title)
                .content(content)
                .author(author)
                .hit(0L)
                .build();

        this.noticeRepository.save(notice);

        return notice;
    }

    public void delete(Notice notice) {
        this.noticeRepository.delete(notice);
    }

    public Notice modify(Notice notice, String title, String content, NoticeCategory category) {

        notice = notice.toBuilder()
                .category(category)
                .title(title)
                .content(content)
                .build();

        this.noticeRepository.save(notice);

        return notice;
    }

    public Notice hit(Long id) {
        Notice notice = this.getNotice(id);

        notice = notice.toBuilder()
                .hit(notice.getHit() + 1)
                .build();

        this.noticeRepository.save(notice);

        return notice;
    }

    public long maxId() {
        return noticeRepository.findMaxId();
    }

    public Notice getPreviousNotice(Long id) {
        Optional<Notice> preNotice = this.noticeRepository.findPreviousNotice(id);
        if (preNotice.isPresent()) {
            return preNotice.get();
        } else {
            // thymeleaf null 처리를 위해 null return
            return null;
        }
    }

    public Notice getNextNotice(Long id) {
        Optional<Notice> nextNotice = this.noticeRepository.findNextNotice(id);
        if (nextNotice.isPresent()) {
            return nextNotice.get();
        } else {
            // thymeleaf null 처리를 위해 null return
            return null;
        }
    }

    public Page<Notice> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
        return this.noticeRepository.findAllByKeyword(kw, pageable);
    }
}
