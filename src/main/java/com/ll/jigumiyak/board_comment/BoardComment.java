package com.ll.jigumiyak.board_comment;

import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board_recomment.BoardRecomment;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class BoardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<BoardRecomment> recommentList;

    @ManyToOne
    private SiteUser author;
}
