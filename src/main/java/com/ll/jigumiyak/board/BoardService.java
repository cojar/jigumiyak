package com.ll.jigumiyak.board;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if(board.isPresent()) {
            return board.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }
    public Board create(String subject, String content, SiteUser siteUser) {
        Board b = new Board();
        b.setSubject(subject);
        b.setContent(content);
        b.setCreateDate(LocalDateTime.now());
        b.setAuthor(siteUser);
        this.boardRepository.save(b);
        return b;
    }

    public Page<Board> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.boardRepository.findAll(pageable);
    }

    public void modify(Board board, String subject, String content) {
        board.setSubject(subject);
        board.setContent(content);
        board.setModifyDate(LocalDateTime.now());
        this.boardRepository.save(board);
    }

    public void delete(Board board) {
        this.boardRepository.delete(board);
    }
}
