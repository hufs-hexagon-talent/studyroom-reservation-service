package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginId)); // 스프링 시큐리티가..

        return new org.springframework.security.core.userdetails.User(
                user.getLoginId(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getIsAdmin() ? "ROLE_ADMIN" : "ROLE_USER"))
        );
    }
}