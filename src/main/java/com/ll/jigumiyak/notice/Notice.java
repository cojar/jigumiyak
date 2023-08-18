package com.ll.jigumiyak.notice;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.notice_category.NoticeCategory;
import com.ll.jigumiyak.notice_comment.NoticeComment;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notice extends BaseEntity {

    @Column(length = 30)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
    private List<NoticeComment> commentList;

    @ManyToOne
    private NoticeCategory category;

    @ManyToOne
    private SiteUser author;

    private Long hit;
}
