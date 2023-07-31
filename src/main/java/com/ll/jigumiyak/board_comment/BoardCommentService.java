package com.ll.jigumiyak.board_comment;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BoardCommentService {
    private final BoardCommentRepository boardCommentRepository;

    public void create(Board board, String content, SiteUser author) {
        BoardComment comment = new BoardComment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setModifyDate(LocalDateTime.now());
        comment.setAuthor(author);
        comment.setBoard(board);
        this.boardCommentRepository.save(comment);
    }
}
