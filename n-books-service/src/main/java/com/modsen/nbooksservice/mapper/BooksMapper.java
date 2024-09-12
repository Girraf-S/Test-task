package com.modsen.nbooksservice.mapper;

import com.modsen.nbooksservice.entity.Book;
import com.modsen.nbooksservice.entity.BookArchive;
import com.modsen.nbooksservice.model.BookArchiveResponse;
import com.modsen.nbooksservice.model.BookRequest;
import com.modsen.nbooksservice.model.BookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BooksMapper {

    Book toBook(BookRequest bookRequest, Long userId, boolean available);

    Book update(@MappingTarget Book book, BookRequest bookRequest);

    BookResponse toBookResponse(Book book);

    BookArchiveResponse toBookArchiveResponse(BookArchive bookArchive);
}
