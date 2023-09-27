package com.bookend.security;

import com.bookend.login.domain.entity.User;
import com.bookend.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> byUsername = userRepository.findByUsername(username);
        System.out.println("byUsername : " + byUsername);
        return byUsername.map(PrincipalDetails::new).orElse(null);
    }
}
