package com.ll.jigumiyak.board;

import com.ll.jigumiyak.DataNotFoundException;
import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<Board> getList(int page, String kw, String kwc, int size) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));

        if (kwc.equals("title")) {
            return this.boardRepository.findSubjectByKeyword(kw, pageable);
        } else if(kwc.equals("content")) {
            return this.boardRepository.findContentByKeyword(kw, pageable);
        } else if(kwc.equals("titleandcontent")) {
            return this.boardRepository.findSubjectAndContentByKeyword(kw, pageable);
        } else if(kwc.equals("author")) {
            return this.boardRepository.findAuthorByKeyword(kw, pageable);
        }

        return this.boardRepository.findAllByKeyword(kw, pageable);
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

    public void vote(Board board, SiteUser siteUser) {
        board.getVoter().add(siteUser);
        this.boardRepository.save(board);
    }

    private Specification<Board> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Board> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Board, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Board, BoardComment> a = q.join("commentList", JoinType.LEFT);
                Join<BoardComment, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("loginId"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("loginId"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }
}
