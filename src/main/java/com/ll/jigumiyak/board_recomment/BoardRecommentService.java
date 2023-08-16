package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BoardRecommentService {
    private final BoardRecommentRepository boardRecommentRepository;

    public BoardRecomment create(BoardComment boardComment, String content, SiteUser author){
        BoardRecomment recomment = new BoardRecomment();
        recomment.setContent(content);
        recomment.setCreateDate(LocalDateTime.now());
        recomment.setComment(boardComment);
        recomment.setAuthor(author);
        this.boardRecommentRepository.save(recomment);
        return recomment;
    }
}
