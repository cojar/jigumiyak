package com.ll.jigumiyak.file;

import com.ll.jigumiyak.base.BaseEntity;
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
public class GenFile extends BaseEntity {

    private String primaryPath;

    private String secondaryPath;

    private String uploader;

    private String date;

    private String ext;
}
