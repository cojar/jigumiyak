package com.ll.jigumiyak.board;

import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<BoardComment> commentList;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    private Integer vote;

    private Long hit;
}
