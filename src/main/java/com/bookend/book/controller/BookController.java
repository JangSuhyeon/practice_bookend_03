package com.bookend.book.controller;

import com.bookend.book.domain.dto.BookRequestDto;
import com.bookend.book.domain.dto.BookReviewResponseDto;
import com.bookend.book.domain.dto.ReviewRequestDto;
import com.bookend.book.service.BookService;
import com.bookend.security.PrincipalDetails;
import com.bookend.security.dto.LoginUser;
import com.bookend.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookController {

    @Value("${aladin.url}")
    private String ALADIN_URL;

    private final BookService bookService;

    // 독후감 작성 화면으로
    @GetMapping("/write")
    public String goToBookWrite() {
        return "book/write";
    }

    // 알라딘 Open API를 이용하여 도서 검색
    @PostMapping(value = "/search", produces = "application/json")
    public ResponseEntity<String> searchBooks(@RequestBody HashMap<String, Object> search) {
        RestTemplate restTemplate = new RestTemplate();

        // 도서 검색 결과 요청
        URI aladinUri = UriComponentsBuilder
                .fromUriString(ALADIN_URL)
                .queryParam("Query", search.get("searchKeyword"))
                .queryParam("QueryType", "Title")
                .queryParam("MaxResults", "10")
                .queryParam("start", "1")
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "JS")
                .queryParam("Version", "20131101")
                .build()
                .encode(StandardCharsets.UTF_8).toUri();

        return restTemplate.getForEntity(aladinUri, String.class);
    }

    // 독후감 저장
    @PostMapping(value = "/write")
    public ResponseEntity<String> saveBook(@RequestBody BookRequestDto bookRequestDto, @AuthenticationPrincipal Object principalUser, @LoginUser SessionUser googleUser) {

        // 저장
        bookService.save(bookRequestDto, principalUser, googleUser);

        return ResponseEntity.ok("성공");
    }

    // 독후감 상세 화면으로
    @GetMapping("/{reviewId}")
    public String goToBookWrite(@PathVariable("reviewId") Long reviewId,
                                @LoginUser SessionUser user,
                                @AuthenticationPrincipal Object principalUser,
                                Model model) {

        // 현재 로그인한 이용자의 userId (수정 버튼 노출 여부)
        Long id;
        if (principalUser instanceof UserDetails) { // 게스트 계정
            id = ((PrincipalDetails) principalUser).getUser().getId();
        }else { // 구글 계정
            id = user.getId();
        }
        model.addAttribute("loginUserId", id);

        // 독후감 상세 정보 조회
        BookReviewResponseDto review = bookService.findByReviewId(reviewId);
        model.addAttribute("review", review);

        return "book/detail";
    }

    @GetMapping("/modify/{reviewId}")
    public String modifyReview(@PathVariable Long reviewId, Model model) {
        BookReviewResponseDto review = bookService.findByReviewId(reviewId);
        model.addAttribute("review", review);
        return "book/modify";
    }

    @PostMapping("/update")
    public String updateReview(BookRequestDto review) {
        bookService.saveReview(review);
        return "redirect:/book/" + review.getReviewId();
    }

}
