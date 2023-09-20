package com.bookend.book.domain.dto;

import com.bookend.book.domain.Book;
import com.bookend.book.domain.BookReview;
import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Builder
@Getter
public class BookReviewResponseDto {

    private Book book;
    private Long reviewId;
    private String shortReview;
    private String longReview;
    private int score;
    private String regDt;

    public static BookReviewResponseDto toDto(BookReview bookReview) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy년 M월 dd일");
        return BookReviewResponseDto.builder()
                .book(bookReview.getBook())
                .reviewId(bookReview.getReviewId())
                .shortReview(bookReview.getShortReview())
                .longReview(bookReview.getLongReview())
                .score(bookReview.getScore())
                .regDt(sdf.format(bookReview.getRegDt()))
                .build();
    }

}
