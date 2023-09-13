package com.bookend.book.domain;

import com.bookend.book.domain.dto.BookRequestDto;
import com.bookend.book.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column
    private String title;
    private String author;
    private String publisher;
    private String cover;

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
