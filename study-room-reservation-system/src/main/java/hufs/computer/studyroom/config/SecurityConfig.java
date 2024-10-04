package hufs.computer.studyroom.config;

import hufs.computer.studyroom.security.CustomUserDetailsService;
import hufs.computer.studyroom.security.JwtAccessDeniedHandler;
import hufs.computer.studyroom.security.jwt.JWTFilter;
import hufs.computer.studyroom.security.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;


import java.util.Collections;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final String jwtAccessCategory;
    private final String jwtRefreshCategory;
    private final String jwtHeader;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;
    private final CustomUserDetailsService userDetailsService;
    @Qualifier("jwtAuthenticationEntryPoint")
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_RESIDENT = "ROLE_RESIDENT";


    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                          JWTUtil jwtUtil,
                          @Value("${spring.jwt.access.category}") String jwtAccessCategory,
                          @Value("${spring.jwt.refresh.category}") String jwtRefreshCategory,
                          @Value("${spring.jwt.header}") String jwtHeader,
                          @Value("${spring.jwt.access.expiration}") Long accessTokenExpiration,
                          @Value("${spring.jwt.refresh.expiration}") Long refreshTokenExpiration,
                          CustomUserDetailsService userDetailsService,
                          AuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler

    ) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.jwtAccessCategory = jwtAccessCategory;
        this.jwtRefreshCategory = jwtRefreshCategory;
        this.jwtHeader = jwtHeader;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
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


        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

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
                                        "/users/me/**").hasAnyAuthority(ROLE_USER, ROLE_RESIDENT, ROLE_ADMIN)
                                .requestMatchers(
                                        "/users/**").hasAuthority(ROLE_ADMIN)

                                // Reservation
                                .requestMatchers(
                                        "/reservations/by-date/**",
                                        "/reservations/partitions/by-date/**").permitAll()
                                .requestMatchers(
                                        "/reservations/**").hasAnyAuthority(ROLE_USER, ROLE_ADMIN)
                                // Policy
                                .requestMatchers(
                                        "/policies/**").hasAnyAuthority(ROLE_ADMIN)
                                // PolicySchedule
                                .requestMatchers(
                                        "/schedules/available-dates").permitAll()
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
                                        "/otp/**"
                                ).hasAnyAuthority(ROLE_USER, ROLE_ADMIN)

                                // check-in
                                .requestMatchers(
                                        "/check-in/**"
                                ).hasAnyAuthority(ROLE_RESIDENT,ROLE_ADMIN)

                                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                );

        // JWT 필터 검증 추가
        http
                .addFilterBefore(new JWTFilter(
                            jwtUtil,
                            jwtHeader,
                            userDetailsService), UsernamePasswordAuthenticationFilter.class);

        // 예외 처리
        http
                .exceptionHandling((exception) ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint) // customEntryPoint
                                .accessDeniedHandler(jwtAccessDeniedHandler));          // customAccessDeniedHandler


        http
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션을 사용하지 않음

        return http.build();
    }


}
