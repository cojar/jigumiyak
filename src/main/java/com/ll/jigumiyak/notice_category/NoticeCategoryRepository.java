package com.ll.jigumiyak.notice_category;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NoticeCategoryRepository extends JpaRepository<NoticeCategory, Long> {
    Optional<NoticeCategory> findByName(String Name);
}
