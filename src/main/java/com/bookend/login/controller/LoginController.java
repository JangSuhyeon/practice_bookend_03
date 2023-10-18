package com.bookend.login.controller;

import com.bookend.login.service.LoginService;
import com.bookend.login.domain.dto.UserResponseDto;
import com.bookend.security.LoginUser;
import com.bookend.login.domain.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.UnknownHostException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    // 로그인 화면으로
    @GetMapping("/page")
    public String goToLogin(@LoginUser SessionUser user, Model model) {
        return "login/login";
    }

    // 게스트 로그인
    @GetMapping("/guest/join")
    public ResponseEntity<UserResponseDto> guestLogin() throws UnknownHostException {

        // User에 로그인 기록 남기기
        UserResponseDto user = loginService.guestSave();

       return ResponseEntity.ok(user);
    }
}
