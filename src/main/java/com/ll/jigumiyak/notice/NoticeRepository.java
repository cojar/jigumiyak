package com.ll.jigumiyak.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 키워드와 카테고리 둘 다 주어질때
//    @Query("SELECT n FROM Notice n " +
//            "WHERE ((:keywordCategory = 'title' AND n.title LIKE %:keyword%) " +
//            "OR (:keywordCategory = 'content' AND n.content LIKE %:keyword%) " +
//            "OR (:keywordCategory = 'author' AND n.author LIKE %:keyword%) " +
//            "OR (:keywordCategory = 'title+content' AND (n.title LIKE %:keyword% OR n.content LIKE %:keyword%))) " +
//            "AND (n.category.name = :category)")
    Page<Notice> searchByKeywordAndCategory(String keyword,
                                            String keywordCategory,
                                            String category,
                                            Pageable pageable);

    // 키워드만 주어질때
//    @Query("SELECT n FROM Notice n " +
//            "WHERE (:keywordCategory = 'title' AND n.title LIKE %:keyword%) " +
//            "OR (:keywordCategory = 'content' AND n.content LIKE %:keyword%) " +
//            "OR (:keywordCategory = 'author' AND n.author LIKE %:keyword%) " +
//            "OR (:keywordCategory = 'title+content' AND (n.title LIKE %:keyword% OR n.content LIKE %:keyword%))")
    Page<Notice> searchByKeyword(String keyword,
                                 String keywordCategory,
                                 String category,
                                 Pageable pageable);

    //카테고리만 주어질때
//    @Query("SELECT n FROM Notice n " +
//            "WHERE (n.category.name = :category)")
    Page<Notice> searchByCategory(String keyword,
                                  String keywordCategory,
                                  String category,
                                  Pageable pageable);
}
