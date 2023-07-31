package com.ll.jigumiyak.notice_comment;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.notice.Notice;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeCommentService {
    private final NoticeCommentRepository noticeCommentRepository;

    public NoticeComment create(Notice notice, String content, SiteUser siteUser) {
        NoticeComment comment = new NoticeComment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setNotice(notice);
        comment.setAuthor(siteUser);
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
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.noticeCommentRepository.save(comment);
    }

    public void delete(NoticeComment comment) {
        this.noticeCommentRepository.delete(comment);
    }

    public List<NoticeComment> getCommentList(){
        return noticeCommentRepository.findAll();
    }
}
