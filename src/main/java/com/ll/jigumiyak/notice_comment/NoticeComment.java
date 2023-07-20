package com.ll.jigumiyak.notice_comment;

import com.ll.jigumiyak.notice.Notice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NoticeComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Notice notice;
}
