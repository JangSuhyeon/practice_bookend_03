package com.bookend.book.domain;

import com.bookend.book.domain.dto.BookRequestDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Builder
@Entity
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookId;

    @Column
    private int score;
    private String shortReview;
    private String longReview;
    private Date modDt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    // Todo Book과 조인
    private Book book;

    // dto -> entity
    public static BookReview toEntity(BookRequestDto bookRequestDto) {
        return BookReview.builder()
                .bookId(bookRequestDto.getBookId())
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
