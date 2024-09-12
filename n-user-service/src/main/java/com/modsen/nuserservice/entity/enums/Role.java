package com.modsen.nuserservice.entity.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

    ADMIN(Set.of(
            Permission.BOOKS_WRITE,
            Permission.BOOKS_READ,
            Permission.BOOKS_TAKE,
            Permission.ACTIVATE_USERS
    )),
    LIBRARIAN(Set.of(
            Permission.BOOKS_WRITE,
            Permission.BOOKS_ARCHIVE,
            Permission.BOOKS_READ
    )),
    READER(Set.of(
            Permission.BOOKS_READ,
            Permission.BOOKS_TAKE
    ));

    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream().map(permission ->
                new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
    }
}
