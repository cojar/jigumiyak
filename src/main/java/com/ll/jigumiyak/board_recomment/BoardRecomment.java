package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.board_comment.BoardComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BoardRecomment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private BoardComment comment;
}
