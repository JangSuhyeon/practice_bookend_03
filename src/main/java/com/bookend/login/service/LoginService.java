package com.bookend.login.service;

import com.bookend.login.domain.Role;
import com.bookend.login.domain.dto.UserResponseDto;
import com.bookend.login.domain.entity.User;
import com.bookend.login.repository.UserRepository;
import com.bookend.login.domain.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;

    public UserResponseDto guestSave() throws UnknownHostException {

        int guestCnt = userRepository.countByRole(Role.GUEST);
        String ip = String.valueOf(InetAddress.getLocalHost());
        String encodePwd = passwordEncoder.encode("test010");

        User user =  User.builder()
                .username("GUEST"+guestCnt)
                .password(encodePwd)
                .name("게스트" + guestCnt)
                .role(Role.GUEST)
                .ip(ip)
                .enabled(true)
                .build();

        // User 저장
        userRepository.save(user);
        httpSession.invalidate(); // 현재 세션을 무효화 시킴
        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 사용자 정보 저장

        return UserResponseDto.toDto(user); // entity -> dto
    }
}
