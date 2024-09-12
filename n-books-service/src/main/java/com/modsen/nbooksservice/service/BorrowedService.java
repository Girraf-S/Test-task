package com.modsen.nbooksservice.service;

import com.modsen.nbooksservice.entity.Book;
import com.modsen.nbooksservice.entity.BookArchive;
import com.modsen.nbooksservice.exception.AppException;
import com.modsen.nbooksservice.repository.BookArchiveRepository;
import com.modsen.nbooksservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BorrowedService {

    private final BookArchiveRepository bookArchiveRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void takeBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new AppException("Book with id " + id + " not found", HttpStatus.BAD_REQUEST)
        );
        book.setAvailable(false);
        book.setUserId(Long.valueOf(
                AuthUtil.extractClaimStringValue(
                        SecurityContextHolder.getContext().getAuthentication(), "id"
                )));
        bookRepository.save(book);
    }

    @Transactional
    public void returnBook(Long id) {
        BookArchive book = bookArchiveRepository.findById(id).orElseThrow(
                () -> new AppException("Book with id " + id + " not found", HttpStatus.BAD_REQUEST)
        );
        book.setAvailable(true);
        book.setUserId(null);
        bookArchiveRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Boolean isCorrectId(Long bookId) {
        return bookArchiveRepository.findById(bookId).orElseThrow(
                () -> new AppException("Not found book with id" + bookId, HttpStatus.BAD_REQUEST)
        ).getUserId().equals(
                Long.valueOf(AuthUtil.extractClaimStringValue(
                                SecurityContextHolder.getContext().getAuthentication(), "id"
                        )
                )
        );
    }
}
