package com.ll.jigumiyak.inquiry;

import com.ll.jigumiyak.base.BaseEntity;
import com.ll.jigumiyak.user.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Inquiry extends BaseEntity {

    @ManyToOne
    private SiteUser inquirer;
}
