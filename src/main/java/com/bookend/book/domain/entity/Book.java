package com.bookend.book.domain.entity;

import com.bookend.book.domain.dto.BookReviewRequestDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column
    private String title;
    private String author;
    private String publisher;
    private String cover;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Todo ???
    private List<Review> reviewList = new ArrayList<>();

    public static Book toEntity(BookReviewRequestDto bookReviewRequestDto) {
        return Book.builder()
                .isbn(bookReviewRequestDto.getIsbn())
                .title(bookReviewRequestDto.getTitle())
                .author(bookReviewRequestDto.getAuthor())
                .publisher(bookReviewRequestDto.getPublisher())
                .cover(bookReviewRequestDto.getCover())
                .build();
    }

}
