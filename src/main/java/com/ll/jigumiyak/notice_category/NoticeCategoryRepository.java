package com.ll.jigumiyak.notice_category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeCategoryRepository extends JpaRepository<NoticeCategory, Long> {
    Optional<NoticeCategory> findByName(String Name);
}
