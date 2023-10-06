package com.bookend.book.repository;

import com.bookend.book.domain.BookReview;
import com.bookend.security.dto.SessionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    BookReview findByReviewId(Long reviewId);

    List<BookReview> findAllByUserId(Long id);
}
