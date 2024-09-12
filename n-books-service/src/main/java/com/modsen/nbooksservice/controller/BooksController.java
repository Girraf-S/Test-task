package com.modsen.nbooksservice.controller;

import com.modsen.nbooksservice.model.BookArchiveResponse;
import com.modsen.nbooksservice.model.BookRequest;
import com.modsen.nbooksservice.model.BookResponse;
import com.modsen.nbooksservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BooksController {

    private final BookService bookService;

    @Operation(
            summary = "Returned book by id",
            description = "Roles with access: Admin, Reader, Librarian"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('books:read')")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(
            summary = "Returned book by isbn",
            description = "Roles with access: Admin, Reader, Librarian"
    )
    @GetMapping("/isbn/{isbn}")
    @PreAuthorize("hasAuthority('books:read')")
    public BookResponse getBookByISBN(@PathVariable String isbn) {
        return bookService.getBookByISBN(isbn);
    }

    @Operation(
            summary = "Returned all books",
            description = "Roles with access: Admin, Reader, Librarian"
    )
    @GetMapping
    @PreAuthorize("hasAuthority('books:read')")
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @Operation(
            summary = "Returned archive book by id",
            description = "Roles with access: Librarian"
    )
    @GetMapping("/archive/{id}")
    @PreAuthorize("hasAuthority('books:archive')")
    public BookArchiveResponse getArchiveBookById(@PathVariable Long id) {
        return bookService.getArchiveBookById(id);
    }

    @Operation(
            summary = "Returned archive books",
            description = "Roles with access: Librarian"
    )
    @GetMapping("/archive")
    @PreAuthorize("hasAuthority('books:archive')")
    public Page<BookArchiveResponse> getAllArchiveBooks(Pageable pageable) {
        return bookService.getAllArchiveBooks(pageable);
    }

    @Operation(
            summary = "Create book",
            description = "Roles with access: Admin, Librarian"
    )
    @PostMapping
    @PreAuthorize("hasAuthority('books:write')")
    public void createBook(@RequestBody BookRequest bookRequest) {
        bookService.createBook(bookRequest);
    }

    @Operation(
            summary = "Update book",
            description = "Roles with access: Admin, Librarian"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('books:write')")
    public void updateBook(@PathVariable Long id,
                           @RequestBody BookRequest bookRequest){
        bookService.updateBook(bookRequest, id);
    }

    @Operation(
            summary = "Delete book",
            description = "Roles with access: Admin, Librarian"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('books:write')")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }

    @Operation(
            summary = "Returned all taken books",
            description = "Roles with access: Admin, Reader"
    )
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('books:take')")
    public List<BookArchiveResponse> getAllTakenBooks() {
        return bookService.getAllTakenBooksByUserId();
    }
}
