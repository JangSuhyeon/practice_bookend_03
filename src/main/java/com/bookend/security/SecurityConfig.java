package com.bookend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // ajax POST 호출을 위함
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/css/**", "/images/**", "/js/**", "/login/guest/join", "/login").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login/page")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .successHandler(
                                        new AuthenticationSuccessHandler() {
                                            @Override
                                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                                System.out.println("authentication : " + authentication.getName());
                                                response.sendRedirect("/"); // 인증이 성공한 후에는 root로 이동
                                            }
                                        }
                                )
                                .failureHandler(
                                        new AuthenticationFailureHandler() {
                                            @Override
                                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                                System.out.println("exception : " + exception.getMessage());
                                                response.sendRedirect("/login");
                                            }
                                        }
                                )
                                .permitAll()
                );
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
//                                .userInfoEndpoint(userInfo ->
//                                        userInfo.userService(customOAuth2UserService)
//                                )
//                );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}






