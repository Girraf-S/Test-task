package com.modsen.nuserservice.mapper;

import com.modsen.nuserservice.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {
    private final PasswordEncoder passwordEncoder;

    @Named(value = "encode")
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Named(value = "getAuthorities")
    public Set<String> getAuthorities(Role role) {
        return role.getAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
