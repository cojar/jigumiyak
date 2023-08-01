package com.ll.jigumiyak.board_recomment;

import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class BoardRecomment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime modifyDate;

    @ManyToOne
    private BoardComment comment;

    @ManyToOne
    private SiteUser author;
}
