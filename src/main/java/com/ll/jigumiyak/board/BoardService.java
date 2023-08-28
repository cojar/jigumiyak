package com.ll.jigumiyak.board;

import com.ll.jigumiyak.DataNotFoundException;
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
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Board getBoard(Long id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }

    public Board create(String subject, String content, SiteUser author) {

        Board board = Board.builder()
                .subject(subject)
                .content(content)
                .hit(0L)
                .author(author)
                .build();

        this.boardRepository.save(board);

        return board;
    }

    public Page<Board> getList(int page, String kw, String kwc, int size, String order) {
        List<Sort.Order> sorts = new ArrayList<>();

        if (order.equals("hit")) {
            sorts.add(Sort.Order.desc("hit"));
        } else if (order.equals("vote")) {
            sorts.add(Sort.Order.desc("vote"));
        } else if (order.equals("old")) {
            sorts.add(Sort.Order.asc("createDate"));
        } else {
            sorts.add(Sort.Order.desc("createDate"));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        if (kwc.equals("title")) {
            return this.boardRepository.findSubjectByKeyword(kw, pageable);
        } else if (kwc.equals("content")) {
            return this.boardRepository.findContentByKeyword(kw, pageable);
        } else if (kwc.equals("titleandcontent")) {
            return this.boardRepository.findSubjectAndContentByKeyword(kw, pageable);
        } else if (kwc.equals("author")) {
            return this.boardRepository.findAuthorByKeyword(kw, pageable);
        }

        return this.boardRepository.findAllByKeyword(kw, pageable);
    }

    public Page<Board> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
        return this.boardRepository.findAllByKeyword(kw, pageable);
    }

    public void modify(Board board, String subject, String content) {

        board = board.toBuilder()
                .subject(subject)
                .content(content)
                .build();

        this.boardRepository.save(board);
    }

    public void delete(Board board) {
        this.boardRepository.delete(board);
    }

    public void vote(Board board, SiteUser siteUser) {
        if (board.getVoter().contains(siteUser)) {
            board.getVoter().remove(siteUser);
        } else {
            board.getVoter().add(siteUser);
        }
        this.boardRepository.save(board);
    }

    public void updateVote(Board board) {
        int vote = board.getVoter().size();

        board = board.toBuilder()
                .vote(vote)
                .build();

        this.boardRepository.save(board);
    }

    public Board hit(Long id) {

        Board board = this.getBoard(id);

        board = board.toBuilder()
                .hit(board.getHit() + 1)
                .build();

        this.boardRepository.save(board);

        return board;
    }
}
