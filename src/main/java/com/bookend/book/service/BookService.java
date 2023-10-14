package com.bookend.book.service;

import com.bookend.book.domain.Book;
import com.bookend.book.domain.BookReview;
import com.bookend.book.domain.dto.BookRequestDto;
import com.bookend.book.domain.dto.BookReviewResponseDto;
import com.bookend.book.domain.dto.ReviewRequestDto;
import com.bookend.book.repository.BookRepository;
import com.bookend.book.repository.BookReviewRepository;
import com.bookend.login.domain.entity.User;
import com.bookend.login.repository.UserRepository;
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
    private final UserRepository userRepository;

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
        if (principalUser instanceof UserDetails) { // 게스트 계정
            id = ((PrincipalDetails) principalUser).getUser().getId();
        }else { // 구글 계정
            id = user.getId();
        }
        System.out.println("id : " + id);

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

        BookReview bookReview = bookReviewRepository.findByReviewId(reviewId);  // 리뷰 가져오기
        String userNm = userRepository.findById(bookReview.getUserId()).map(User::getName).orElse(null);    // 리뷰 작성자 이름 가져오기
        BookReviewResponseDto bookReviewResponseDto = BookReviewResponseDto.toDto(bookReview);  // Entity - > DTO
        bookReviewResponseDto.setUserNm(userNm);    // DTO에 리뷰 작성자 이름 넣기

        return bookReviewResponseDto;
    }

    // 독후감 수정
    public void saveReview(BookRequestDto review) {

        // DTO -> Entity
        BookReview bookReview = BookReview.toEntity(review);
        bookReviewRepository.save(bookReview);

    }
}
