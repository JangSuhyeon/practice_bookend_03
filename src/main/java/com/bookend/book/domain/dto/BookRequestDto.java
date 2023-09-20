package com.bookend.book.domain.dto;

import lombok.Getter;

@Getter
public class BookRequestDto {

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String cover;
    private int score;
    private String shortReview;
    private String longReview;

    private Long bookId;

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

}