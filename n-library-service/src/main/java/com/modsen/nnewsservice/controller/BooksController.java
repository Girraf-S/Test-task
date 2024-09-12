package com.modsen.nnewsservice.controller;

import com.modsen.nnewsservice.entity.enums.Status;
import com.modsen.nnewsservice.model.BorrowedBookResponse;
import com.modsen.nnewsservice.service.BorrowedBooksService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/library")
public class BooksController {

    private final BorrowedBooksService borrowedBooksService;

    @Operation(
            summary = "Returned a books, taken by users",
            description = "Roles with access: Librarian"
    )
    @GetMapping
    @PreAuthorize("hasAuthority('books:archive')")
    public Page<BorrowedBookResponse> getBooks(@RequestParam(required = false)  Status status, Pageable pageable){
        return borrowedBooksService.getBooksByStatus(status, pageable);
    }

    @Operation(
            summary = "Returned a book by id, taken by users",
            description = "Roles with access: Librarian"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('books:archive')")
    public BorrowedBookResponse getBooks(@PathVariable Long id){
        return borrowedBooksService.getBookById(id);
    }

    @Operation(
            summary = "Change book status to waiting taking",
            description = "Roles with access: Admin, Reader"
    )
    @PostMapping("/take/{id}")
    @PreAuthorize("hasAuthority('books:take')")
    public void takeBook(@PathVariable Long id) {
        borrowedBooksService.takeBook(id);
    }

    @Operation(
            summary = "Change book status to waiting extending",
            description = "Roles with access: Admin, Reader"
    )
    @PutMapping("/extend/{id}")
    @PreAuthorize("hasAuthority('books:take')")
    public void extendBook(@PathVariable Long id) {
        borrowedBooksService.extendBook(id);
    }


    @Operation(
            summary = "Change book status to waiting returned",
            description = "Roles with access: Admin, Reader"
    )
    @PutMapping("/return/{id}")
    @PreAuthorize("hasAuthority('books:take')")
    public void returnBook(@PathVariable Long id) {
        borrowedBooksService.returnBook(id);
    }

    @Operation(
            summary = "Change book status to taking",
            description = "Roles with access: Librarian"
    )
    @PutMapping("/apply/taking/{id}")
    @PreAuthorize("hasAuthority('books:archive')")
    public void applyTaking(@PathVariable Long id){
        borrowedBooksService.applyTaking(id);
    }


    @Operation(
            summary = "Change book status to taking and extending returning date by 3 days",
            description = "Roles with access: Librarian"
    )
    @PutMapping("/apply/extending/{id}")
    @PreAuthorize("hasAuthority('books:archive')")
    public void applyExtending(@PathVariable Long id){
        borrowedBooksService.applyExtending(id);
    }

    @Operation(
            summary = "Change book status to returning",
            description = "Roles with access: Librarian"
    )
    @DeleteMapping("/apply/returning/{id}")
    @PreAuthorize("hasAuthority('books:archive')")
    public void applyReturning(@PathVariable Long id){
        borrowedBooksService.applyReturning(id);
    }
}
