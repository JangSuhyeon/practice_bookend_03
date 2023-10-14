package com.bookend.book.domain.dto;

import com.bookend.book.domain.Book;
import com.bookend.book.domain.BookReview;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.text.SimpleDateFormat;

@Builder
@Getter
@ToString
public class ReviewRequestDto {

    private Book book;
    private Long reviewId;
    private Long userId;
    private String userNm;
    private String shortReview;
    private String longReview;
    private int score;
    private Boolean openYn;
    private String regDt;

    public static ReviewRequestDto toDto(BookReview bookReview) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy년 M월 dd일");
        return ReviewRequestDto.builder()
                .book(bookReview.getBook())
                .reviewId(bookReview.getReviewId())
                .userId(bookReview.getUserId())
                .shortReview(bookReview.getShortReview())
                .longReview(bookReview.getLongReview())
                .score(bookReview.getScore())
                .openYn(bookReview.getOpenYn())
                .regDt(sdf.format(bookReview.getRegDt()))
                .build();
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

}
