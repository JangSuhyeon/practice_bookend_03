package com.bookend.book.domain;

import com.bookend.book.domain.dto.BookRequestDto;
import lombok.Builder;

import javax.persistence.*;

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

    // Todo Book과 조인

    public static BookReview toEntity(BookRequestDto bookRequestDto) {
        return BookReview.builder()
                .bookId(bookRequestDto.getBookId())
                .score(bookRequestDto.getScore())
                .shortReview(bookRequestDto.getShortReview())
                .longReview(bookRequestDto.getLongReview())
                .build();
    }

}
