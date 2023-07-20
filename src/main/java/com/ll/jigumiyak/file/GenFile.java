package com.ll.jigumiyak.file;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GenFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
