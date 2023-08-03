package com.ll.jigumiyak.board_comment;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardCommentService {
    private final BoardCommentRepository boardCommentRepository;

    public BoardComment create(Board board, String content, SiteUser author) {
        BoardComment comment = new BoardComment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setModifyDate(LocalDateTime.now());
        comment.setAuthor(author);
        comment.setBoard(board);
        this.boardCommentRepository.save(comment);
        return comment;
    }

    public BoardComment getBoardComment(Long id) {
        Optional<BoardComment> comment = this.boardCommentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("comment not found");
        }
    }

    public void modify(BoardComment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.boardCommentRepository.save(comment);
    }

    public void delete(BoardComment boardComment) {
        this.boardCommentRepository.delete(boardComment);
    }

    public void vote(BoardComment boardComment, SiteUser siteUser) {
        boardComment.getVoter().add(siteUser);
        this.boardCommentRepository.save(boardComment);
    }
}
