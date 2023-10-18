package com.bookend.book.controller;

import com.bookend.book.domain.dto.BookReviewRequestDto;
import com.bookend.book.domain.dto.BookReviewResponseDto;
import com.bookend.book.service.BookReviewService;
import com.bookend.security.PrincipalDetails;
import com.bookend.security.LoginUser;
import com.bookend.login.domain.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class BookReviewController {

    @Value("${aladin.url}")
    private String ALADIN_URL;

    private final BookReviewService bookReviewService;

    // 독후감 작성 화면으로
    @GetMapping("/write")
    public String goToReviewWrite() {
        return "review/write";
    }

    // 알라딘 Open API를 이용하여 도서 검색
    @PostMapping(value = "/book/search", produces = "application/json")
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
    public ResponseEntity<String> saveReview(@RequestBody BookReviewRequestDto bookReviewRequestDto,
                                             @AuthenticationPrincipal Object principalUser,
                                             @LoginUser SessionUser googleUser) {

        // 저장
        bookReviewService.saveBookReview(bookReviewRequestDto, principalUser, googleUser);

        return ResponseEntity.ok("성공");
    }

    // 독후감 상세 화면으로
    @GetMapping("/{reviewId}")
    public String goToReviewDetail(@PathVariable("reviewId") Long reviewId,
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
        BookReviewResponseDto review = bookReviewService.findReviewByReviewId(reviewId);
        model.addAttribute("review", review);

        return "review/detail";
    }

    @GetMapping("/modify/{reviewId}")
    public String goToReviewModify(@PathVariable Long reviewId, Model model) {
        BookReviewResponseDto review = bookReviewService.findReviewByReviewId(reviewId);
        model.addAttribute("review", review);
        return "review/modify";
    }

    @PostMapping("/update")
    public String updateReview(@RequestBody BookReviewRequestDto review) {
        System.out.println("review : " + review.toString());
        bookReviewService.updateReview(review);
        return "redirect:/review/" + review.getReviewId();
    }

}
