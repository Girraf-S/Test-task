package com.modsen.nnewsservice.model;

import com.modsen.nnewsservice.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {
    private Long id;
    private LocalDateTime borrowedDate;
    private LocalDateTime returnDate;
    private Status status;
}
