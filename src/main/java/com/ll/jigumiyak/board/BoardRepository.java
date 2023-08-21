package com.ll.jigumiyak.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAll(Pageable pageable);

    @Query("select "
            + "distinct b "
            + "from Board b "
            + "left outer join SiteUser u1 on b.author=u1 "
            + "where "
            + "   b.subject like %:kw% "
            + "   or b.content like %:kw% "
            + "   or u1.loginId like %:kw% ")
    Page<Board> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    @Query("select "
            + "distinct b "
            + "from Board b "
            + "where "
            + "   b.subject like %:kw% ")
    Page<Board> findSubjectByKeyword(@Param("kw") String kw, Pageable pageable);

    @Query("select "
            + "distinct b "
            + "from Board b "
            + "where "
            + "   b.content like %:kw% ")
    Page<Board> findContentByKeyword(@Param("kw") String kw, Pageable pageable);

    @Query("select "
            + "distinct b "
            + "from Board b "
            + "where "
            + "   b.subject like %:kw% "
            + "   or b.content like %:kw% ")
    Page<Board> findSubjectAndContentByKeyword(@Param("kw") String kw, Pageable pageable);

    @Query("select "
            + "distinct b "
            + "from Board b "
            + "left outer join SiteUser u1 on b.author=u1 "
            + "where "
            + "   u1.loginId like %:kw% ")
    Page<Board> findAuthorByKeyword(@Param("kw") String kw, Pageable pageable);
}
