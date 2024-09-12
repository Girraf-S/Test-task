package com.modsen.nnewsservice.entity;

import com.modsen.nnewsservice.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {

    @Id
    private Long id;

    @Column
    private LocalDateTime borrowedDate;

    @Column
    private LocalDateTime returnDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
