package com.modsen.nbooksservice.service;

import com.modsen.nbooksservice.entity.Book;
import com.modsen.nbooksservice.entity.BookArchive;
import com.modsen.nbooksservice.exception.AppException;
import com.modsen.nbooksservice.mapper.BooksMapper;
import com.modsen.nbooksservice.model.BookArchiveResponse;
import com.modsen.nbooksservice.model.BookRequest;
import com.modsen.nbooksservice.model.BookResponse;
import com.modsen.nbooksservice.repository.BookArchiveRepository;
import com.modsen.nbooksservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookArchiveRepository bookArchiveRepository;
    private final BooksMapper booksMapper;

    @Transactional
    public void createBook(BookRequest bookRequest) {
        Book book = booksMapper.toBook(bookRequest, getUserIdFromSecurityContext(), true);

        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(BookRequest bookRequest, Long id) {
        Book book = getBookByIdOrThrowException(id);

        book = booksMapper.update(book, bookRequest);

        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = getBookByIdOrThrowException(id);
        return booksMapper.toBookResponse(book);
    }

    @Transactional(readOnly = true)
    public BookResponse getBookByISBN(String isbn) {
        Book book = bookRepository.findBookByISBN(isbn).orElseThrow(
                () -> new AppException("Book with isbn " + isbn + " not found", HttpStatus.BAD_REQUEST)
        );
        return booksMapper.toBookResponse(book);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(booksMapper::toBookResponse);
    }

    @Transactional(readOnly = true)
    public BookArchiveResponse getArchiveBookById(Long id) {
        BookArchive bookArchive = getArchiveBookByIdOrThrowException(id);
        return booksMapper.toBookArchiveResponse(bookArchive);
    }

    @Transactional(readOnly = true)
    public Page<BookArchiveResponse> getAllArchiveBooks(Pageable pageable) {
        return bookArchiveRepository.findAll(pageable).map(booksMapper::toBookArchiveResponse);
    }

    @Transactional(readOnly = true)
    public List<BookArchiveResponse> getAllTakenBooksByUserId() {
        return bookArchiveRepository.findByUserId(getUserIdFromSecurityContext())
                .stream().map(booksMapper::toBookArchiveResponse).toList();
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    private Book getBookByIdOrThrowException(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No book with id " + id)
        );
    }

    private BookArchive getArchiveBookByIdOrThrowException(Long id) {
        return bookArchiveRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No book with id " + id)
        );
    }


    private static Long getUserIdFromSecurityContext() {
        return Long.parseLong(
                AuthUtil.extractClaimStringValue(SecurityContextHolder.getContext().getAuthentication(), "id")
        );
    }
}
