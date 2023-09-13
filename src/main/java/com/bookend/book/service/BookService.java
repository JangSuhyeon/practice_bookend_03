package com.bookend.book.service;

import com.bookend.book.domain.Book;
import com.bookend.book.domain.BookReview;
import com.bookend.book.domain.dto.BookRequestDto;
import com.bookend.book.repository.BookRepository;
import com.bookend.book.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;

    // 독후감 저장
    public void save(BookRequestDto bookRequestDto) {

        Long bookId;

        // 기존에 저장된 도서인지 확인
        Book foundBook = bookRepository.findByIsbn(bookRequestDto.getIsbn());

        // 기존에 저장된 도서가 아니면 저장
        if (foundBook == null) {
            Book book = Book.toEntity(bookRequestDto);
            Book savedBook = bookRepository.save(book);
            bookId = savedBook.getId();
        }else{
            // 기존에 저장된 도서이면 찾은 도서의 id를 가져오기
            bookId = foundBook.getId();
        }

        // 독후감 저장
        bookRequestDto.setBookId(bookId);
        BookReview bookReview = BookReview.toEntity(bookRequestDto);
        bookReviewRepository.save(bookReview);
    }
}
