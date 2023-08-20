package com.ll.jigumiyak.address;

import com.ll.jigumiyak.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address extends BaseEntity {

    private Integer zoneCode;

    @Column(length = 50)
    private String mainAddress;

    @Column(length = 50)
    private String subAddress;
}
