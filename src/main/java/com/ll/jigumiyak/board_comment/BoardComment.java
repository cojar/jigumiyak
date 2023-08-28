package com.ll.jigumiyak.board_comment;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.board.Board;
import com.ll.jigumiyak.board_recomment.BoardRecomment;
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
public class BoardComment extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<BoardRecomment> recommentList;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;
}
