//package com.test.studyroomreservationsystem.security.jwt;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.test.studyroomreservationsystem.dto.util.ErrorResponseDto;
//import com.test.studyroomreservationsystem.dto.auth.LoginRequestDto;
//import com.test.studyroomreservationsystem.dto.auth.LoginResponseDto;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.Getter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.*;
//
//import static org.springframework.http.MediaType.*;
//
//
//@Slf4j
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//    private final AuthenticationManager authenticationManager;
//    private final JWTUtil jwtUtil;
//    @Getter
//    @Value("${spring.jwt.access.category}") private String jwtAccessCategory;
//    @Value("${spring.jwt.refresh.category}") private  String jwtRefreshCategory;
//    @Value("${spring.jwt.access.expiration}") private Long accessTokenExpiration;
//    @Value("${spring.jwt.refresh.expiration}")private Long refreshTokenExpiration;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public LoginFilter(AuthenticationManager authenticationManager,
//                       JWTUtil jwtUtil,
//                       String jwtAccessCategory, String jwtRefreshCategory,
//                       Long accessTokenExpiration, Long refreshTokenExpiration
//    ) {
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//        this.jwtAccessCategory = jwtAccessCategory;
//        this.jwtRefreshCategory = jwtRefreshCategory;
//        this.accessTokenExpiration = accessTokenExpiration;
//        this.refreshTokenExpiration = refreshTokenExpiration;
//
//        if (authenticationManager == null) {
//            log.error("AuthenticationManager 가 작동 오류");
//        } else {
//            log.info("AuthenticationManager 가 정상 작동");
//        }
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request,
//                                                HttpServletResponse response) throws AuthenticationException {
//        log.trace("2차 필터(Login Filter) : [attemptAuthentication] 들어왔습니다.");
//
//        //HTTP 메서드 방식은 POST, json 타입의 데이터로만 로그인을 진행한다.
//        if (!isPost(request)){
//            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
//
//            // todo : response 바디에 error 메시지 담아서 응답
//            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), "Unsupported Method : " + request.getMethod());
//            writeResponse(response, errorResponse);
//
//            throw new AuthenticationServiceException("Unsupported Method : " + request.getMethod());
//        }
//        if (!isApplicationJson(request)){
//            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
//
//            // todo : response 바디에 error 메시지 담아서 응답
//            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), "Unsupported Media Type: " + request.getContentType());
//            writeResponse(response, errorResponse);
//            throw new AuthenticationServiceException("Unsupported Media Type: " + request.getContentType());
//        }
//        LoginRequestDto loginRequestDto;
//        try {
//            loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
//        } catch (IOException e) {
//            log.trace("2차 필터(Login Filter) : [파싱 에러] request 파싱 에러");
//
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//
//            // todo : response 바디에 error 메시지 담아서 응답
//            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Error parsing login request");
//            writeResponse(response, errorResponse);
//            throw new AuthenticationServiceException("Error parsing login request ", e);
//        }
//        // 정상적으로 request 파싱
////        log.trace("2차 필터(Login Filter) : [파싱 성공] request 파싱 성공");
//        String username = loginRequestDto.getUsername();
//        String password = loginRequestDto.getPassword();
////        log.trace("2차 필터(Login Filter) : [인증 시도] {} 가 인증 시도 ", username);
//
//
//        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
//        UsernamePasswordAuthenticationToken authToken
//                = new UsernamePasswordAuthenticationToken(username, password);
//        //token에 담은 검증을 위한 AuthenticationManager로 전달
//        log.trace("2차 필터(Login Filter) 작동 종료 ");
//        // 그럼 매니저가 로그인 처리를 하고 성공하면 successfulAuthentication 메소드 실행
//        return authenticationManager.authenticate(authToken);
//    }
//
//    //로그인 성공 핸들러 : 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authentication) throws IOException, ServletException {
//
//        log.info("로그인 성공 핸들러 : [로그인 성공] authenticationManager 검증을 성공하였습니다.");
//        log.info("로그인 성공 핸들러  : 로그인 성공 핸들러 작동 합니다.");
//        String username = authentication.getName();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//        String role = auth.getAuthority();
//        log.trace("로그인 성공 핸들러  : [디버깅용 테스트]  {}",auth.getAuthority());
//        log.trace("로그인 성공 핸들러  : JWT 토큰을 생성합니다. ");
//
//        // JWT 토큰 생성
//        String accessToken = jwtUtil.createAccessJwt(jwtAccessCategory, username, role, accessTokenExpiration * 1000); // 밀리초 -> 초
//        String refreshToken = jwtUtil.createRefreshJwt(jwtRefreshCategory, username, refreshTokenExpiration * 1000);
//        // LoginResponseDto 객체 생성
//        LoginResponseDto loginResponse = new LoginResponseDto(accessToken, refreshToken);
//
//        log.trace("로그인 성공 핸들러  : JWT 토큰이 생성 되었습니다. ");
//        log.trace("access 토큰 : {}", accessToken);
//        log.trace("refresh 토큰 : {}", refreshToken);
//
//
//        // 응답 설정
//        //      response header
//        response.setStatus(HttpStatus.OK.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("utf-8");
//
//        //      response body
//        PrintWriter writer = response.getWriter();
//        writer.print(objectMapper.writeValueAsString(loginResponse));
//
//        // 로그 출력
//        log.trace("로그인 성공 핸들러  : [response 확인] {}",response.getStatus());
//        log.trace("로그인 성공 핸들러  : [response 확인] {}",response.getContentType());
//        log.trace("[정상작동]login 핸들러가 작동이 끝납니다.");
//    }
//
//    //로그인 실패시 실행하는 메소드
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request,
//                                              HttpServletResponse response,
//                                              AuthenticationException failed) throws IOException {
//        log.info("[ 로그인 실패 핸들러 작동 ]");
//        if (response.getStatus() != HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE &&
//                response.getStatus() != HttpServletResponse.SC_NOT_FOUND &&
//                response.getStatus() != HttpServletResponse.SC_FORBIDDEN)
//        {
//            // 기본적으로 401 Unauthorized 응답
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "Unauthenticated , Failed to find user");
//            writeResponse(response, errorResponse);
//        }
//
//    }
//
//
//    private boolean isPost(HttpServletRequest request) {
//        if(HttpMethod.POST.toString().equals(request.getMethod())) {
//            return true;
//        }
//        return false;
//    }
//    private boolean isApplicationJson(HttpServletRequest request) {
//        String contentType = request.getContentType();
//        if (APPLICATION_JSON_VALUE.toLowerCase().equals(contentType.toLowerCase())||
//                APPLICATION_JSON_UTF8_VALUE.toLowerCase().equals(contentType.toLowerCase())) {
//            return true;
//        }
//        return false;
//    }
//
//    private void writeResponse(HttpServletResponse response, ErrorResponseDto errorResponse) {
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter writer = null;
//        try {
//            writer = response.getWriter();
//            writer.print(objectMapper.writeValueAsString(errorResponse));
//            writer.flush();  // 데이터를 클라이언트에게 즉시 전송
//        } catch (IOException e) {
//            log.error("응답 쓰기 중 에러 발생", e);
//        } finally {
//            if (writer != null) {
//                writer.close();  // PrintWriter를 안전하게 닫음
//            }
//        }
//    }
//}
