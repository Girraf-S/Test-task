package com.modsen.nnewsservice.mapper;

import com.modsen.nnewsservice.entity.BorrowedBook;
import com.modsen.nnewsservice.model.BorrowedBookResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowedBookMapper {

    BorrowedBookResponse toResponse(BorrowedBook book);
}
