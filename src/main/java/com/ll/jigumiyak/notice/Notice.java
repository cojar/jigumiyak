package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.notice_category.NoticeCategory;
import com.ll.jigumiyak.notice_comment.NoticeComment;
import com.ll.jigumiyak.user.SiteUser;
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
    @Column(length = 20)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
    private List<NoticeComment> commentList;
    @ManyToOne
    private NoticeCategory category;
    @ManyToOne
    private SiteUser author;
}
