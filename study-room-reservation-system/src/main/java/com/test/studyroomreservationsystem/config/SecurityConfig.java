package com.test.studyroomreservationsystem.config;


import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
            .httpBasic(AbstractHttpConfigurer::disable) // HTTP 기본 인증 비활성화
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/sign-in","/auth/sign-up").permitAll() // 로그인, 회원가입 인증 없이 허용
                    .requestMatchers("/user/**").hasRole("USER") // User 용 API 는 유저 인증 받아야함
                    .requestMatchers("/admin/**").hasRole("ADMIN") // Admin 용 API 는 어드민 인증 받아야함
                    .anyRequest().authenticated()) // 그 외 모든 요청은 인증 필요
            .logout((logout) -> logout
                    .invalidateHttpSession(true))
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 관리 정책 설정
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}