package com.modsen.nuserservice.entity.enums;

import lombok.Getter;

@Getter
public enum Permission {
    BOOKS_READ("books:read"),
    BOOKS_WRITE("books:write"),
    BOOKS_TAKE("books:take"),
    BOOKS_ARCHIVE("books:archive"),
    ACTIVATE_USERS("activate:users");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }
}
