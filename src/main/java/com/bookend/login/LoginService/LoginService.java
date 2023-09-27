package com.bookend.login.LoginService;

import com.bookend.login.domain.Role;
import com.bookend.login.domain.UserResponseDto;
import com.bookend.login.domain.entity.User;
import com.bookend.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        return UserResponseDto.toDto(user); // entity -> dto
    }

}
