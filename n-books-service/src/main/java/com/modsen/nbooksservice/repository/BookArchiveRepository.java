package com.modsen.nbooksservice.repository;

import com.modsen.nbooksservice.entity.BookArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookArchiveRepository extends JpaRepository<BookArchive, Long> {
    List<BookArchive> findByUserId(Long userId);
}
