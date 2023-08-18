package com.ll.jigumiyak.receipt;

import com.ll.jigumiyak.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class Receipt extends BaseEntity {

}
