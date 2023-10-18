package com.bookend.security;

import com.bookend.login.domain.entity.User;
import com.bookend.login.repository.UserRepository;
import com.bookend.login.domain.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

/**
 * OAuth2 로그인 이후 반환된 사용자 정보를 처리
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService(); // DefaultOAuth2UserService는 OAuth2UserService 구현체로서, 이 변수를 이용하여 기본 OAuth2 작업을 수행할 수 있음.
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // 사용자 정보를 가져옴, userRequest는 OAuth2 로그인 요청과 관련된 정보를 포함하고 있음.
        System.out.println("oAuth2User : " + oAuth2User);   // Name, Granted Authorities, User Attributes

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 현재 로그인 진행 중인 서비스를 구분하는 코드 (google)
        System.out.println("registrationId : " + registrationId);   // google

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // OAuth2 로그인 진행 시 키가 되는 필드값
        System.out.println("userNameAttributeName : " + userNameAttributeName); // sub

        // 로그인 IP
        String ip;
        try {
             ip = String.valueOf(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes(), ip); // userRequest 안에 담긴 사용자 정보를 OAuthAttributes로 변환
        System.out.println("attributes : " + attributes);

        User user = saveOrUpdate(attributes);
        httpSession.invalidate(); // 현재 세션을 무효화 시킴
        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 사용자 정보 저장

        // DefaultOAuth2User 객체를 반환함으로써, OAuth2 로그인 기능을 구현한 웹 애플리케이션은 권한 부여, 사용자 정보 제공, 세션 관리 등의 기능을 제공할 수 있음.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), // 사용자 Role의 key를 가져와 권한 설정
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 사용자 정보 저장 및 업데이트
    private User saveOrUpdate(OAuthAttributes attributes) {
        // 기존에 있는 사용자 이면 정보 업데이트
        User user = userRepository.findByUsername(attributes.getUsername())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
