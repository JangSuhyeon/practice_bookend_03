package com.bookend.login.domain;

import com.bookend.login.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private String username;
    private String password;

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
