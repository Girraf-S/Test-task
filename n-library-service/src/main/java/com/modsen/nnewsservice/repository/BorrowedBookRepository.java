package com.modsen.nnewsservice.repository;

import com.modsen.nnewsservice.entity.BorrowedBook;
import com.modsen.nnewsservice.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    Page<BorrowedBook> findAllByStatus(Status status, Pageable pageable);
}
