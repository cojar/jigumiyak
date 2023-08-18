package com.ll.jigumiyak.board_recomment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRecommentRepository extends JpaRepository<BoardRecomment, Long> {
}
