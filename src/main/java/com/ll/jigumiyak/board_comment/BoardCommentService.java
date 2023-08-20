package com.ll.jigumiyak.board_comment;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardCommentService {
    private final BoardCommentRepository boardCommentRepository;

    public BoardComment create(Board board, String content, SiteUser author) {

        BoardComment comment = BoardComment.builder()
                .content(content)
                .author(author)
                .board(board)
                .build();

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

        comment = comment.toBuilder()
                .content(content)
                .build();

        this.boardCommentRepository.save(comment);
    }

    public void delete(BoardComment boardComment) {
        this.boardCommentRepository.delete(boardComment);
    }

    public void vote(BoardComment boardComment, SiteUser siteUser) {
        if (boardComment.getVoter().contains(siteUser)) {
            boardComment.getVoter().remove(siteUser);
        } else {
            boardComment.getVoter().add(siteUser);
        }
        this.boardCommentRepository.save(boardComment);
    }

    public Page<BoardComment> getList(Board board, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        return this.boardCommentRepository.findAllByBoard(board, pageable);
    }

}
