package com.modsen.nuserservice.controller;

import com.modsen.nuserservice.model.BookResponse;
import com.modsen.nuserservice.model.UserResponse;
import com.modsen.nuserservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(
            summary = "Return account info",
            description = "Roles with access: authenticated"
    )
    @GetMapping
    public UserResponse getCurrentProfileInfo() {
        return accountService.getCurrentProfileInfo();
    }

    @Operation(
            summary = "Return all books taking by user",
            description = "Roles with access: Reader"
    )
    @GetMapping("/books")
    @PreAuthorize("hasAuthority('books:take')")
    public List<BookResponse> getTakenBooks(){
        return accountService.getTakenBooks();
    }
}
