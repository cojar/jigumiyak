package com.ll.jigumiyak.notice_comment;

import com.ll.jigumiyak.notice.Notice;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class NoticeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @ManyToOne
    private Notice notice;

    @ManyToOne
    private SiteUser author;
}
