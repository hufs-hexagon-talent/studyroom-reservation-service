package com.test.studyroomreservationsystem.config;

import com.test.studyroomreservationsystem.security.jwt.CustomLogoutFilter;
import com.test.studyroomreservationsystem.security.jwt.JWTFilter;
import com.test.studyroomreservationsystem.security.jwt.JWTUtil;
import com.test.studyroomreservationsystem.security.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;


import java.util.Collections;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {return configuration.getAuthenticationManager();}
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http
//                .headers(.disable());
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

//                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("access"));

                        return configuration;
                    }
                }));

        http
                .csrf(AbstractHttpConfigurer::disable); // REST API 이므로 CSRF 보호는 비활성화
        http
                .formLogin(AbstractHttpConfigurer::disable); // Form Login 비활성화
        http
                .httpBasic(AbstractHttpConfigurer::disable); // HTTP Basic 비활성화
        http
                .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()   // Swagger UI와 API 문서 경로 허용
                    .requestMatchers("/reissue","/login","/user/sign-up").permitAll() // [로그인], [회원가입] ,[엑세스 재발급] 인증 없이 허용
                    .requestMatchers("/user/**").hasRole("USER")                          // User 용 API 2는 유저 인증 받아야함
                    .requestMatchers("/admin/**").hasRole("ADMIN")                        // Admin 용 API 는 어드민 인증 받아야함
                    .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                );

        // JWT 필터 검증 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http
                .addFilterBefore(logoutFilter(), LogoutFilter.class);
        http
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션을 사용하지 않음

        return http.build();
    }

    @Bean
    CustomLogoutFilter logoutFilter()throws Exception {
        return new CustomLogoutFilter(jwtUtil);

    }

}
