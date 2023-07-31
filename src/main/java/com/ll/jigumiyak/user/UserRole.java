package com.ll.jigumiyak.user;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("admin"),
    COMMUNITY_MANAGER("community_manager"),
    SELLER("seller"),
    USER("user"),
    TEMP_USER("temp_user");

    private String value;

    UserRole(String value) {
        this.value = value;
    }
}
