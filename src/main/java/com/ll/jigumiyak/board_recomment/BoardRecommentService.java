package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardRecommentService {
    private final BoardRecommentRepository boardRecommentRepository;

    public BoardRecomment create(BoardComment boardComment, String content, SiteUser author) {
        BoardRecomment recomment = new BoardRecomment();
        recomment.setContent(content);
        recomment.setCreateDate(LocalDateTime.now());
        recomment.setModifyDate(LocalDateTime.now());
        recomment.setAuthor(author);
        recomment.setComment(boardComment);
        this.boardRecommentRepository.save(recomment);
        return recomment;
    }

    public BoardRecomment getBoardRecomment(Long id) {
        Optional<BoardRecomment> recomment = this.boardRecommentRepository.findById((id));
        if (recomment.isPresent()) {
            return recomment.get();
        } else {
            throw new DataNotFoundException("recomment not found");
        }
    }
}
