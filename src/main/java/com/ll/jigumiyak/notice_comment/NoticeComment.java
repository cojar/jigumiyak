package com.ll.jigumiyak.notice_comment;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.notice.Notice;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NoticeComment extends BaseEntity {

    private String content;

    @ManyToOne
    private Notice notice;

    @ManyToOne
    private SiteUser author;
}
