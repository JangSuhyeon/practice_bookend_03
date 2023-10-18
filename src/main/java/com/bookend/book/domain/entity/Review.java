package com.bookend.book.domain.entity;

import com.bookend.book.domain.dto.BookReviewRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Boolean openYn;
    private int score;
    private String shortReview;
    private String longReview;
    private Date modDt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    // Book과 조인
    @ManyToOne
    @JoinColumn(name = "bookId", insertable = false, updatable = false)
    private Book book;

    // dto -> entity
    public static Review toEntity(BookReviewRequestDto bookReviewRequestDto) {
        return Review.builder()
                .bookId(bookReviewRequestDto.getBookId())
                .userId(bookReviewRequestDto.getUserId())
                .openYn(bookReviewRequestDto.getOpenYn())
                .score(bookReviewRequestDto.getScore())
                .shortReview(bookReviewRequestDto.getShortReview())
                .longReview(bookReviewRequestDto.getLongReview())
                .build();
    }

    // 엔티티가 저장되기 전 호출하여 등록일자 세팅
    @PrePersist
    protected void setRegDt() {
        regDt = new Date();
    }

}
