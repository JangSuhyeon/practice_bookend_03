package com.bookend.login.domain;

import com.bookend.login.domain.entity.User;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SessionUser {
    private Long id;
    private String name;
    private String username;
    private String picture;

    public SessionUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.picture = user.getPicture();
    }
}
