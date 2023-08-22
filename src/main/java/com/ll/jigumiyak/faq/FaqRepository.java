package com.ll.jigumiyak.faq;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.inquiry.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    Page<Faq> findAll(Pageable pageable);

    @Query("select "
            + "distinct f "
            + "from Faq f "
            + "where "
            + "   f.category = :category")
    List<Faq> findAllByCategory(@Param("category") String category);
}
