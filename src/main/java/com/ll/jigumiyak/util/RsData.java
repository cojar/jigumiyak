package com.ll.jigumiyak.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RsData<T> {

    private String code;

    private String message;

    private T data;
}