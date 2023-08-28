package com.ll.jigumiyak.notice_comment;

import com.ll.jigumiyak.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {
    Page<NoticeComment> findAllByNotice (Notice notice, Pageable pageable);
}
