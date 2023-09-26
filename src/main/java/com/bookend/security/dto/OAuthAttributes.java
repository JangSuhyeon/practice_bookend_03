package com.bookend.security.dto;

import com.bookend.login.domain.Role;
import com.bookend.login.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * OAuth2UserService를 통해 가져온 OAuth2User의 attributes를 담을 클래스
 */
@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String ip;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes, String ip) {
        // Todo registrationId를 가지고 어떤 소셜 로그인인지 분기
        if (registrationId.equals("google")) {
            return ofGoogle(userNameAttributeName, attributes, ip);
        } else {
            return ofGuest(userNameAttributeName, attributes, ip);
        }
    }

    // Google OAuth 인증을 통해 얻은 사용자 정보
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, String ip) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .ip(ip)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 게스트
    private static OAuthAttributes ofGuest(String userNameAttributeName, Map<String, Object> attributes, String ip) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email("GUEST")
                .picture("GUEST")
                .ip(ip)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .ip(ip)
                .role(Role.USER)
                .build();
    }

    public User toGuestEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
