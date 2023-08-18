package com.ll.jigumiyak.board;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.board_comment.BoardComment;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board extends BaseEntity {

    @Column(length = 300)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<BoardComment> commentList;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    private Integer vote;

    private Long hit;
}
