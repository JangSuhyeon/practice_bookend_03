package com.bookend.book.domain.dto;

import com.bookend.book.domain.entity.Book;
import com.bookend.book.domain.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.text.SimpleDateFormat;

@Builder
@Getter
@ToString
public class BookReviewResponseDto {

    private Book book;
    private Long reviewId;
    private Long userId;
    private String userNm;
    private String shortReview;
    private String longReview;
    private int score;
    private Boolean openYn;
    private String regDt;

    public static BookReviewResponseDto toDto(Review review) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy년 M월 dd일");
        return BookReviewResponseDto.builder()
                .book(review.getBook())
                .reviewId(review.getReviewId())
                .userId(review.getUserId())
                .shortReview(review.getShortReview())
                .longReview(review.getLongReview())
                .score(review.getScore())
                .openYn(review.getOpenYn())
                .regDt(sdf.format(review.getRegDt()))
                .build();
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

}
