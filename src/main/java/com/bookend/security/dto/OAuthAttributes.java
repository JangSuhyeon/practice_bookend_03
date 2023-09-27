package com.bookend.security.dto;

import com.bookend.login.domain.Role;
import com.bookend.login.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * OAuth2UserService를 통해 가져온 OAuth2User의 attributes를 담을 클래스
 */
@ToString
@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String username;
    private String picture;
    private String ip;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String ip) {
        // registrationId를 가지고 어떤 소셜 로그인인지 분기
//        if (registrationId.equals("google")) {
            return ofGoogle(userNameAttributeName, attributes, ip);
//        } else {
//
//        }
    }

    // Google OAuth 인증을 통해 얻은 사용자 정보
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, String ip) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .username((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .ip(ip)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .username(username)
                .picture(picture)
                .ip(ip)
                .role(Role.USER)
                .build();
    }
}
