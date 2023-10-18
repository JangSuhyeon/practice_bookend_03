package com.bookend.book.repository;

import com.bookend.book.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);
}
