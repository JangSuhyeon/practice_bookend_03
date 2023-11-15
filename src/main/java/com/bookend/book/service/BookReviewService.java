package com.bookend.book.service;

import com.bookend.book.domain.entity.Book;
import com.bookend.book.domain.entity.Review;
import com.bookend.book.domain.dto.BookReviewRequestDto;
import com.bookend.book.domain.dto.BookReviewResponseDto;
import com.bookend.book.repository.BookRepository;
import com.bookend.book.repository.ReviewRepository;
import com.bookend.login.domain.entity.User;
import com.bookend.login.repository.UserRepository;
import com.bookend.security.PrincipalDetails;
import com.bookend.login.domain.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookReviewService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    // 도서, 독후감 저장
    public void saveBookReview(BookReviewRequestDto bookReviewRequestDto, Object principalUser, SessionUser guestUser) {

        Long id;
        if (principalUser instanceof OAuth2User) { // 구글 계정
            id = guestUser.getId(); // Todo 왜 googleUser에는 id가 없을까?
        } else if (principalUser instanceof UserDetails) { // 게스트 계정
            id = ((PrincipalDetails) principalUser).getUser().getId();
        } else {
            id = null; // Todo 에러 예외 처리
        }
        bookReviewRequestDto.setUserId(id);

        Long bookId;
        Book foundBook = bookRepository.findByIsbn(bookReviewRequestDto.getIsbn()); // 기존에 저장된 도서인지 확인
        if (foundBook == null) {
            Book book = Book.toEntity(bookReviewRequestDto);
            Book savedBook = bookRepository.save(book); // 기존에 저장된 도서가 아니면 저장
            bookId = savedBook.getBookId();
        }else{
            // 기존에 저장된 도서이면 찾은 도서의 id를 가져오기
            bookId = foundBook.getBookId();
        }
        bookReviewRequestDto.setBookId(bookId);

        // 독후감 저장
        Review review = Review.toEntity(bookReviewRequestDto);
        reviewRepository.save(review);
    }

    // 독후감 목록 조회
    public List<BookReviewResponseDto> findAllReviews(Object principalUser, SessionUser user) {
        Long id;
        if (principalUser instanceof UserDetails) { // 게스트 계정
            id = ((PrincipalDetails) principalUser).getUser().getId();
        }else { // 구글 계정
            id = user.getId();
        }

        List<Review> bookList = reviewRepository.findAllByUserId(id);

        List<BookReviewResponseDto> bookResponseDtoList = new ArrayList<>();
        for (Review review : bookList) {
            BookReviewResponseDto bookReviewResponseDto = BookReviewResponseDto.toDto(review);
            bookResponseDtoList.add(bookReviewResponseDto);
        }

        return bookResponseDtoList;
    }

    // 독후감 상세 정보 조회
    public BookReviewResponseDto findReviewByReviewId(Long reviewId) {

        Review review = reviewRepository.findByReviewId(reviewId);  // 리뷰 가져오기
        String userNm = userRepository.findById(review.getUserId()).map(User::getName).orElse(null);    // 리뷰 작성자 이름 가져오기
        BookReviewResponseDto bookReviewResponseDto = BookReviewResponseDto.toDto(review);  // Entity - > DTO
        bookReviewResponseDto.setUserNm(userNm);    // DTO에 리뷰 작성자 이름 넣기

        return bookReviewResponseDto;
    }

    // 독후감 수정
    public void updateReview(BookReviewRequestDto modifiedReview) {

        Review review = reviewRepository.findByReviewId(modifiedReview.getReviewId());  // 리뷰 가져오기
        review.modify(modifiedReview);  // 데이터 업데이트

        reviewRepository.save(review);

    }

    public void deleteReview(BookReviewRequestDto review) {
        reviewRepository.deleteById(review.getReviewId());
    }

    public List<BookReviewResponseDto> findByBook_TitleContaining(String searchReview) {
        System.out.println("searchReview : " + searchReview);
        List<Review> reviewList = reviewRepository.findByBook_TitleContaining(searchReview);
        System.out.println("reviewList : " + reviewList);
        List<BookReviewResponseDto> bookResponseDtoList = new ArrayList<>();
        for (Review review : reviewList) {
            BookReviewResponseDto bookReviewResponseDto = BookReviewResponseDto.toDto(review);
            bookResponseDtoList.add(bookReviewResponseDto);
        }
        return bookResponseDtoList;
    }
}
