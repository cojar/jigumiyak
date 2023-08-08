package com.ll.jigumiyak.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 키워드와 카테고리 둘 다 주어질때
    @Query("SELECT n FROM Notice n " +
            "WHERE ((:keywordCategory = 'title' AND n.title LIKE %:keyword%) " +
            "OR (:keywordCategory = 'content' AND n.content LIKE %:keyword%) " +
            "OR (:keywordCategory = 'author' AND n.author.loginId LIKE %:keyword%) " +
            "OR (:keywordCategory = 'all' AND (n.title LIKE %:keyword% OR n.content LIKE %:keyword%))) " +
            "AND (n.category.name = :category)")
    Page<Notice> searchByKeywordAndCategory(@Param("keyword") String keyword,
                                            @Param("keywordCategory") String keywordCategory,
                                            @Param("category") String category,
                                            Pageable pageable);

    // 키워드만 주어질때
    @Query("SELECT n FROM Notice n " +
            "WHERE (:keywordCategory = 'title' AND n.title LIKE %:keyword%) " +
            "OR (:keywordCategory = 'content' AND n.content LIKE %:keyword%) " +
            "OR (:keywordCategory = 'author' AND n.author.loginId LIKE %:keyword%) " +
            "OR (:keywordCategory = 'all' AND (n.title LIKE %:keyword% OR n.content LIKE %:keyword%))")
    Page<Notice> searchByKeyword(@Param("keyword") String keyword,
                                 @Param("keywordCategory") String keywordCategory,
                                 Pageable pageable);

    //카테고리만 주어질때
    @Query("SELECT n FROM Notice n " +
            "WHERE (n.category.name = :category)")
    Page<Notice> searchByCategory(@Param("category") String category,
                                  Pageable pageable);

    @Query("SELECT MAX(n.id) FROM Notice n")
    Long findMaxId();
}
