package com.ll.jigumiyak.board_comment;

import com.ll.jigumiyak.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BoardCommentService {
    private final BoardCommentRepository boardCommentRepository;

    public void create(Board board, String content) {
        BoardComment comment = new BoardComment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setModifyDate(LocalDateTime.now());
        comment.setBoard(board);
        this.boardCommentRepository.save(comment);
    }
}
