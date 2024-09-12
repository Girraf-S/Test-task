package com.modsen.nuserservice.controller;

import com.modsen.nuserservice.model.LoginRequest;
import com.modsen.nuserservice.model.RegisterRequest;
import com.modsen.nuserservice.model.TokenResponse;
import com.modsen.nuserservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Login and returning jwt token",
            description = "Roles with access: unauthenticated"
    )
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(
            summary = "Reader registration",
            description = "Roles with access: unauthenticated"
    )
    @PostMapping("/registration/reader")
    public void readerRegistration(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.readerRegistration(registerRequest);
    }

    @Operation(
            summary = "Librarian registration",
            description = "Roles with access: unauthenticated"
    )
    @PostMapping("/registration/librarian")
    public void librarianRegistration(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.librarianRegistration(registerRequest);
    }
}
