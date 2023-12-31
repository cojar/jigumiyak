package com.ll.jigumiyak.security;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum CustomRole {

    SUPER_ADMIN("super_admin", 0, "총관리자"),
    ADMIN("admin", 1, "관리자"),
    SELLER("seller", 2, "판매자"),
    USER("user", 3, "회원"),
    WITHDRAWAL_USER("withdrawal_user", 4, "탈퇴회원"),
    BLACKLIST("blacklist", 5, "블랙리스트");

    private String type;
    private Integer code;
    private String typeKor;
    private static final Map<Integer, String> codeToType = Stream.of(values()).collect(Collectors.toMap(CustomRole::getCode, CustomRole::getType));
    private static final Map<String, Integer> typeToCode = Stream.of(values()).collect(Collectors.toMap(CustomRole::getType, CustomRole::getCode));
    private static final Map<String, String> typeToTypeKor = Stream.of(values()).collect(Collectors.toMap(CustomRole::getType, CustomRole::getTypeKor));

    CustomRole(String type, Integer code, String typeKor) {
        this.type = type;
        this.code = code;
        this.typeKor = typeKor;
    }

    public Integer getDecCode() {
        return (int) Math.pow(2, this.code);
    }

    public static Integer getDecCodeByType(String type) {
        return (int) Math.pow(2, typeToCode.get(type));
    }

    public static String getTypeByCode(int code) {
        return codeToType.get(code);
    }

    public static String getTypeKorByType(String type) {
        return typeToTypeKor.get(type);
    }
}
