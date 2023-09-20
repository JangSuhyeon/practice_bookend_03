package com.bookend.book.repository;

import com.bookend.book.domain.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    BookReview findByReviewId(Long reviewId);
}
