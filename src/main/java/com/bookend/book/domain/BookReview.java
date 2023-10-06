package com.bookend.book.domain;

import com.bookend.book.domain.dto.BookRequestDto;
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
public class BookReview {

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
    public static BookReview toEntity(BookRequestDto bookRequestDto) {
        return BookReview.builder()
                .bookId(bookRequestDto.getBookId())
                .userId(bookRequestDto.getUserId())
                .openYn(bookRequestDto.getOpenYn())
                .score(bookRequestDto.getScore())
                .shortReview(bookRequestDto.getShortReview())
                .longReview(bookRequestDto.getLongReview())
                .build();
    }

    // 엔티티가 저장되기 전 호출하여 등록일자 세팅
    @PrePersist
    protected void setRegDt() {
        regDt = new Date();
    }

}
