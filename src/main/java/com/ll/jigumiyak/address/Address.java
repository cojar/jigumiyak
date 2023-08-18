package com.ll.jigumiyak.address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer zoneCode;

    @Column(length = 50)
    private String mainAddress;

    @Column(length = 50)
    private String subAddress;
}
