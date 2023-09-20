package com.bookend.book.domain;

import com.bookend.book.domain.dto.BookRequestDto;
import com.bookend.book.domain.dto.BookResponseDto;
import com.bookend.book.repository.BookRepository;
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
    private List<BookReview> bookReviewList = new ArrayList<>();

    public static Book toEntity(BookRequestDto bookRequestDto) {
        return Book.builder()
                .isbn(bookRequestDto.getIsbn())
                .title(bookRequestDto.getTitle())
                .author(bookRequestDto.getAuthor())
                .publisher(bookRequestDto.getPublisher())
                .cover(bookRequestDto.getCover())
                .build();
    }

}
