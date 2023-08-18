package com.ll.jigumiyak.security;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum CustomRole {

    ADMIN("admin", 0, "관리자"),
    COMMUNITY_MANAGER("community_manager", 1, "커뮤니티 매니저"),
    SELLER("seller", 2, "판매자"),
    USER("user", 3, "회원"),
    TEMP_USER("temp_user", 4, "임시 유저");

    private String type;
    private Integer code;
    private String typeKor;
    private static final Map<Integer, String> codeToType = Stream.of(values()).collect(Collectors.toMap(CustomRole::getCode, CustomRole::getType));
    private static final Map<String, String> TypeToTypeKor = Stream.of(values()).collect(Collectors.toMap(CustomRole::getType, CustomRole::getTypeKor));

    CustomRole(String type, Integer code, String typeKor) {
        this.type = type;
        this.code = code;
        this.typeKor = typeKor;
    }

    public Integer getDecCode() {
        return (int) Math.pow(2, this.code);
    }

    public static String getTypeByCode(int code) {
        return codeToType.get(code);
    }

    public static String getTypeKorByType(String type) {
        return TypeToTypeKor.get(type);
    }
}
