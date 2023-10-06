package com.bookend.main.controller;

import com.bookend.book.domain.dto.BookReviewResponseDto;
import com.bookend.book.service.BookService;
import com.bookend.security.dto.LoginUser;
import com.bookend.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final BookService bookService;

    @GetMapping("/")
    public String goToMain(@LoginUser SessionUser user, @AuthenticationPrincipal Object principalUser, Model model) {
        if (user != null) {
            model.addAttribute("username", user.getName());
            System.out.println("username : " + user.getName());
        }

        // 독후감 목록
        List<BookReviewResponseDto> bookReviewList = bookService.findAll(principalUser, user);
        model.addAttribute("bookReviewList", bookReviewList);

        return "index";
    }

}
