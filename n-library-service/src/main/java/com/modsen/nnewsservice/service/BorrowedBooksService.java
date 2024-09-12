package com.modsen.nnewsservice.service;

import com.modsen.nnewsservice.entity.BorrowedBook;
import com.modsen.nnewsservice.entity.enums.Status;
import com.modsen.nnewsservice.exception.AppException;
import com.modsen.nnewsservice.mapper.BorrowedBookMapper;
import com.modsen.nnewsservice.model.BorrowedBookResponse;
import com.modsen.nnewsservice.repository.BorrowedBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BorrowedBooksService {

    @Value("${jwt.bearer}")
    private String bearer;
    @Value("${service.books-domain}")
    private String booksDomain;
    private final BorrowedBookRepository borrowedBookRepository;
    private final BorrowedBookMapper borrowedBookMapper;
    private final RestTemplate restTemplate;


    @Transactional
    public void takeBook(Long id) {
        borrowedBookRepository.save(
                BorrowedBook.builder()
                        .id(id)
                        .borrowedDate(null)
                        .returnDate(null)
                        .status(Status.AWAITING_TO_TAKE)
                        .build()
        );
        takeBookFromLibrary(id);
    }

    @Transactional
    public void extendBook(Long id) {
        if (isCorrectId(id)) {
            BorrowedBook book = getBookByIdOrThrowException(id);
            book.setStatus(Status.AWAITING_TO_EXTEND);
            borrowedBookRepository.save(book);
        } else throw new AppException("You doesn't take a book with id " + id, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public void returnBook(Long id) {
        if (isCorrectId(id)) {
            BorrowedBook book = getBookByIdOrThrowException(id);

            book.setStatus(Status.AWAITING_TO_RETURN);

            borrowedBookRepository.save(book);
        } else throw new AppException("You doesn't take a book with id " + id, HttpStatus.BAD_REQUEST);
    }

    @Transactional(readOnly = true)
    public Page<BorrowedBookResponse> getBooksByStatus(Status status, Pageable pageable) {
        return status == null ?
                borrowedBookRepository.findAll(pageable).map(borrowedBookMapper::toResponse) :
                borrowedBookRepository.findAllByStatus(status, pageable).map(borrowedBookMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public BorrowedBookResponse getBookById(Long id) {
        BorrowedBook book = getBookByIdOrThrowException(id);
        return borrowedBookMapper.toResponse(book);
    }

    @Transactional
    public void applyTaking(Long id) {
        BorrowedBook book = getBookByIdOrThrowException(id);
        book.setBorrowedDate(LocalDateTime.now());
        book.setReturnDate(LocalDateTime.now().plusDays(7L));
        book.setStatus(Status.TAKING);
    }

    @Transactional
    public void applyExtending(Long id) {
        BorrowedBook book = getBookByIdOrThrowException(id);
        book.setReturnDate(
                book.getReturnDate().plusDays(3L)
        );
        book.setStatus(Status.TAKING);
    }

    @Transactional
    public void applyReturning(Long id) {
        borrowedBookRepository.deleteById(id);
        returnBookToLibrary(id);
    }

    private BorrowedBook getBookByIdOrThrowException(Long id) {
        return borrowedBookRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No book with id " + id)
        );
    }

    private void takeBookFromLibrary(Long id) {
        changeBookAvailable(id, "take/");
    }

    private void returnBookToLibrary(Long id) {
        changeBookAvailable(id, "return/");
    }

    private void changeBookAvailable(Long id, String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, bearer +
                AuthUtil.extractClaimStringValue(SecurityContextHolder.getContext().getAuthentication(), "jwt")
        );
        restTemplate.exchange(booksDomain + "/api/books/borrowed/" + path + id, HttpMethod.PUT,
                new HttpEntity<>(headers), Void.class);
    }

    private Boolean isCorrectId(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, bearer +
                AuthUtil.extractClaimStringValue(SecurityContextHolder.getContext().getAuthentication(), "jwt")
        );
        return restTemplate.exchange(booksDomain + "/api/books/borrowed/accept-user-id/" + id, HttpMethod.GET,
                new HttpEntity<>(headers), Boolean.class).getBody();
    }
}
