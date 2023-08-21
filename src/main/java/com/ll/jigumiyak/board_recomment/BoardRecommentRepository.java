package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.board_comment.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRecommentRepository extends JpaRepository<BoardRecomment, Long> {
    List<BoardRecomment> findAllByComment(BoardComment boardComment);
}
