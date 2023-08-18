package com.ll.jigumiyak.notice_category;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.notice.Notice;
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
public class NoticeCategory extends BaseEntity {

    @Column
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Notice> noticeList;
}
