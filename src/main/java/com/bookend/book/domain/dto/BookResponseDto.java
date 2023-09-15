package com.bookend.book.domain.dto;

import com.bookend.book.domain.Book;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookResponseDto {

    private Book book;
    private String shortReview;
    private String regDt;

}
