package com.ll.jigumiyak.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAll(Pageable pageable);

    Page<Board> findAll(Specification<Board> spec, Pageable pageable);

    @Query("select "
            + "distinct b "
            + "from Board b "
            + "left outer join SiteUser u1 on b.author=u1 "
            + "left outer join BoardComment a on bc.board=b "
            + "left outer join SiteUser u2 on bc.author=u2 "
            + "where "
            + "   b.subject like %:kw% "
            + "   or b.content like %:kw% "
            + "   or u1.loginId like %:kw% "
            + "   or bc.content like %:kw% "
            + "   or u2.loginId like %:kw% ")
    Page<Board> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
