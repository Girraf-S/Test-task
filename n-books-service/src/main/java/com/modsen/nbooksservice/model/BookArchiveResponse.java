package com.modsen.nbooksservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookArchiveResponse {
    private Long id;
    private String ISBN;
    private String title;
    private String author;
    private Long userId;
}
