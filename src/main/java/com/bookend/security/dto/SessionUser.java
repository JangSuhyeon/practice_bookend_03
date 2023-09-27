package com.bookend.security.dto;

import com.bookend.login.domain.entity.User;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SessionUser {
    private String name;
    private String username;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.picture = user.getPicture();
    }
}
