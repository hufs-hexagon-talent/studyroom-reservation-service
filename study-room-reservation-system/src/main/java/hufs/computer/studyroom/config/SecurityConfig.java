package hufs.computer.studyroom.config;

import hufs.computer.studyroom.domain.auth.security.CustomUserDetailsService;
import hufs.computer.studyroom.domain.auth.security.handler.CustomAccessDeniedHandler;
import hufs.computer.studyroom.domain.auth.security.filter.JWTFilter;
import hufs.computer.studyroom.domain.auth.security.handler.CustomAuthenticationEntryPoint;
import hufs.computer.studyroom.domain.auth.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JWTService jwtService;
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_BLOCKED = "ROLE_BLOCKED";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_RESIDENT = "ROLE_RESIDENT";
    private static final String ROLE_EXPIRED = "ROLE_EXPIRED";


    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();}
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(
                        new AntPathRequestMatcher("/public/**"),
                        new AntPathRequestMatcher("/favicon.ico"),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/v3/api-docs/**")
                );
    }

    // cors 구성
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:3000", "http://localhost:8081",
                        "https://studyroom.computer.hufs.ac.kr", "https://api.studyroom.computer.hufs.ac.kr",
                        "https://studyroom-qa.alpaon.net", "https://api.studyroom-qa.alpaon.net",
                        "https://studyroom.alpaon.net", "https://api.studyroom.alpaon.net"
                )
        );
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.addAllowedHeader("*");
//        config.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {


        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()));
        http
                .csrf(AbstractHttpConfigurer::disable); // REST API 이므로 CSRF 보호는 비활성화
        http
                .formLogin(AbstractHttpConfigurer::disable); // Form Login 비활성화
        http
                .httpBasic(AbstractHttpConfigurer::disable); // HTTP Basic 비활성화
        http
                .addFilterBefore(new JWTFilter(jwtService, customUserDetailsService), UsernamePasswordAuthenticationFilter.class); // JWT 필터 검증 추가
        http
                .authorizeHttpRequests((auth) -> auth
                                // Swagger
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**").permitAll()

                                // Auth
                                .requestMatchers(
                                        "/auth/refresh",
                                        "/auth/login",
                                        "/auth/mail/send",
                                        "/auth/mail/verify"
                                ).permitAll()

                                // User
                                .requestMatchers(
                                        "/users/sign-up",
                                        "/users/reset-password").permitAll()
                                .requestMatchers(
                                        "/users/me/**").hasAnyAuthority(ROLE_USER, ROLE_RESIDENT, ROLE_ADMIN, ROLE_BLOCKED)
                                .requestMatchers(
                                        "/users/**").hasAuthority(ROLE_ADMIN)

                                // Reservation
                                .requestMatchers(
                                        "/reservations/by-date/**",
                                        "/reservations/partitions/by-date/**").permitAll()
                                .requestMatchers(
                                        "/reservations/**").hasAnyAuthority(ROLE_USER, ROLE_ADMIN, ROLE_BLOCKED)

                                // Policy
                                .requestMatchers(
                                        "/policies/**").hasAnyAuthority(ROLE_ADMIN)
                                // PolicySchedule
                                .requestMatchers(
                                        "/schedules/available-dates/**").permitAll()
                                .requestMatchers(
                                        "/schedules/**").hasAnyAuthority(ROLE_ADMIN)


                                // Room
                                .requestMatchers(
                                        "/rooms/policy/by-date/**").permitAll()
                                .requestMatchers(
                                        "/rooms/**").hasAnyAuthority(ROLE_RESIDENT,ROLE_ADMIN)

                                // RoomPartition
                                .requestMatchers(
                                        "/partitions/policy/by-date").permitAll()
                                .requestMatchers(
                                        "/partitions/**").hasAuthority(ROLE_ADMIN)

                                // otp
                                .requestMatchers(
                                        "/check-in/otp"
                                ).hasAnyAuthority(ROLE_USER, ROLE_ADMIN, ROLE_BLOCKED)

                                // check-in
                                .requestMatchers(
                                        "/check-in"
                                ).hasAnyAuthority(ROLE_RESIDENT,ROLE_ADMIN)

                                // banner
                                .requestMatchers(
                                        "/banners").hasAnyAuthority(ROLE_ADMIN)
                                .requestMatchers("/banners/active").permitAll()

                                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                );

        // 예외 처리
        http
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                /**
                                 *  인증되지 않은 요청일 경우
                                 *  SecurityContext 에 등록되지 않았을 때 호출된다.
                                 */
                                .authenticationEntryPoint(customAuthenticationEntryPoint) // customEntryPoint
                                /**
                                 * 인증은 되었으나, 해당 요청에 대한 권한이 없는 사용자인 경우
                                 * .hasRole 로 권한을 검사할 때 권한이 부족하여 요청이 거부되었을 때 호출된다.
                                 */
                                .accessDeniedHandler(customAccessDeniedHandler));          // customAccessDeniedHandler


        http
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션을 사용하지 않음

        return http.build();
    }


}
