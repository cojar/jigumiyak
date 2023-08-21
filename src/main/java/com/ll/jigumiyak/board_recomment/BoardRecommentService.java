package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardRecommentService {

    private final BoardRecommentRepository boardRecommentRepository;

    public BoardRecomment create(BoardComment boardComment, String content, SiteUser author) {

        BoardRecomment recomment = BoardRecomment.builder().content(content).author(author).comment(boardComment).build();

        this.boardRecommentRepository.save(recomment);

        return recomment;
    }

    public BoardRecomment getBoareRecomment(Long id) {
        Optional<BoardRecomment> recomment = this.boardRecommentRepository.findById(id);
        if (recomment.isPresent()) {
            return recomment.get();
        } else {
            throw new DataNotFoundException("recomment not found");
        }
    }

    public void modify(BoardRecomment recomment, String content) {

        recomment = recomment.toBuilder()
                .content(content)
                .build();

        this.boardRecommentRepository.save(recomment);
    }

    public void delete(BoardRecomment boardRecomment) {
        this.boardRecommentRepository.delete(boardRecomment);
    }

    public List<BoardRecomment> getList(BoardComment boardComment) {
        return this.boardRecommentRepository.findAllByComment(boardComment);
    }
}
