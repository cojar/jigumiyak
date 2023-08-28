package com.ll.jigumiyak.notice_comment;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.notice.Notice;
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

@Service
@RequiredArgsConstructor
public class NoticeCommentService {
    private final NoticeCommentRepository noticeCommentRepository;

    public NoticeComment create(Notice notice, String content, SiteUser author) {

        NoticeComment comment = NoticeComment.builder()
                .content(content)
                .author(author)
                .notice(notice)
                .build();

        this.noticeCommentRepository.save(comment);

        return comment;
    }

    public NoticeComment getCommentById(Long id){
       Optional<NoticeComment> comment = noticeCommentRepository.findById(id);
        if (comment.isPresent()){
            return comment.get();
        } else {
            throw new DataNotFoundException("notice not found");
        }
    }

    public void modify(NoticeComment comment, String content) {

        comment = comment.toBuilder()
                .content(content)
                .build();

        this.noticeCommentRepository.save(comment);
    }

    public void delete(NoticeComment comment) {
        this.noticeCommentRepository.delete(comment);
    }

    public List<NoticeComment> getCommentList(){
        return noticeCommentRepository.findAll();
    }

    public Page<NoticeComment> getList (Notice notice, int size, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        return this.noticeCommentRepository.findAllByNotice(notice, pageable);
    }
}
