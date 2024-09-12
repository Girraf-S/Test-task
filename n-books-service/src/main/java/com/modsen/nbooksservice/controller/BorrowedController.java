package com.modsen.nbooksservice.controller;

import com.modsen.nbooksservice.service.BorrowedService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books/borrowed")
public class BorrowedController {

    private final BorrowedService borrowedService;


    @Operation(
            summary = "Validate userID",
            description = "Roles with access: Reader\n" +
                    "endpoint to applying userId"
    )
    @GetMapping("/accept-user-id/{id}")
    @PreAuthorize("hasAuthority('books:take')")
    public Boolean acceptUserId(@PathVariable Long id) {
        return borrowedService.isCorrectId(id);
    }

    @Operation(
            summary = "Replace book in archive",
            description = "Roles with access: Librarian"
    )
    @PutMapping("/take/{id}")
    @PreAuthorize("hasAuthority('books:take')")
    public void takeBook(@PathVariable Long id) {
        borrowedService.takeBook(id);
    }

    @Operation(
            summary = "Replace book from archive",
            description = "Roles with access: Librarian"
    )
    @PutMapping("/return/{id}")
    @PreAuthorize("hasAuthority('books:archive')")
    public void returnBook(@PathVariable Long id) {
        borrowedService.returnBook(id);
    }
}
