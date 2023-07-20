package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.notice_comment.NoticeComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
    private List<NoticeComment> commentList;
}
