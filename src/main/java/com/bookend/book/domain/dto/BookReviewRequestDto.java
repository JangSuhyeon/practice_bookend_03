package com.bookend.book.domain.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BookReviewRequestDto {

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Boolean openYn;
    private String cover;
    private int score;
    private String shortReview;
    private String longReview;

    private Long bookId;

    private Long userId;

    private Long reviewId;

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
