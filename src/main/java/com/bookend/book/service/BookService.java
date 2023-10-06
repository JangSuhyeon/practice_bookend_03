package com.bookend.book.service;

import com.bookend.book.domain.Book;
import com.bookend.book.domain.BookReview;
import com.bookend.book.domain.dto.BookRequestDto;
import com.bookend.book.domain.dto.BookReviewResponseDto;
import com.bookend.book.repository.BookRepository;
import com.bookend.book.repository.BookReviewRepository;
import com.bookend.security.PrincipalDetails;
import com.bookend.security.dto.LoginUser;
import com.bookend.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;

    // 독후감 저장
    public void save(BookRequestDto bookRequestDto, Object principalUser, SessionUser guestUser) {

        Long id;
        if (principalUser instanceof OAuth2User) { // 구글 계정
            id = guestUser.getId(); // Todo 왜 googleUser에는 id가 없을까?
        } else if (principalUser instanceof UserDetails) { // 게스트 계정
            id = ((PrincipalDetails) principalUser).getUser().getId();
        } else {
            id = null; // Todo 에러 예외 처리
        }
        bookRequestDto.setUserId(id);

        Long bookId;
        Book foundBook = bookRepository.findByIsbn(bookRequestDto.getIsbn()); // 기존에 저장된 도서인지 확인
        if (foundBook == null) {
            Book book = Book.toEntity(bookRequestDto);
            Book savedBook = bookRepository.save(book); // 기존에 저장된 도서가 아니면 저장
            bookId = savedBook.getBookId();
        }else{
            // 기존에 저장된 도서이면 찾은 도서의 id를 가져오기
            bookId = foundBook.getBookId();
        }
        bookRequestDto.setBookId(bookId);

        // 독후감 저장
        BookReview bookReview = BookReview.toEntity(bookRequestDto);
        bookReviewRepository.save(bookReview);
    }

    // 독후감 목록 조회
    public List<BookReviewResponseDto> findAll(Object principalUser, SessionUser user) {

        Long id;
        if (principalUser instanceof OAuth2User) { // 구글 계정
            id = user.getId();
        } else if (principalUser instanceof UserDetails) { // 게스트 계정
            id = ((PrincipalDetails) principalUser).getUser().getId();
        } else {
            id = null; // Todo 에러 예외 처리
        }

        List<BookReview> bookList = bookReviewRepository.findAllByUserId(id);

        List<BookReviewResponseDto> bookResponseDtoList = new ArrayList<>();
        for (BookReview bookReview : bookList) {
            BookReviewResponseDto bookReviewResponseDto = BookReviewResponseDto.toDto(bookReview);
            bookResponseDtoList.add(bookReviewResponseDto);
        }

        return bookResponseDtoList;
    }

    // 독후감 상세 정보 조회
    public BookReviewResponseDto findByReviewId(Long reviewId) {

        BookReview bookReview = bookReviewRepository.findByReviewId(reviewId);

        return BookReviewResponseDto.toDto(bookReview);
    }
}
