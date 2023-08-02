package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.notice_category.NoticeCategory;
import com.ll.jigumiyak.notice_comment.NoticeComment;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column
    private LocalDateTime createDate;
    @Column
    private LocalDateTime modifyDate;
    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
    private List<NoticeComment> commentList;
    @ManyToOne
    private NoticeCategory category;
    @ManyToOne
    private SiteUser author;
}
