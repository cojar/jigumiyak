package com.ll.jigumiyak.security;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum CustomRole {

    ADMIN("admin", 0),
    COMMUNITY_MANAGER("community_manager", 1),
    SELLER("seller", 2),
    USER("user", 3),
    TEMP_USER("temp_user", 4);

    private String type;
    private Integer code;
    private static final Map<Integer, String> codeToType = Stream.of(values()).collect(Collectors.toMap(CustomRole::getCode, CustomRole::getType));

    CustomRole(String type, Integer code) {
        this.type = type;
        this.code = code;
    }

    public Integer getDecCode() {
        return (int) Math.pow(2, this.code);
    }

    public static String getTypeByCode(int code) {
        return codeToType.get(code);
    }
}
