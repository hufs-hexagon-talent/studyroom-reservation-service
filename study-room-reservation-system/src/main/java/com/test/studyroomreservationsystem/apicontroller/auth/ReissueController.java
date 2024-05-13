//package com.test.studyroomreservationsystem.apicontroller.auth;
//
//
//import com.test.studyroomreservationsystem.domain.repository.RefreshRepository;
//import com.test.studyroomreservationsystem.security.jwt.JWTUtil;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//
//@Slf4j
//@Tag(name = "Auth", description = "인증 관련")
//@RestController
//public class ReissueController {
//
//    private final JWTUtil jwtUtil;
//    private final RefreshRepository refreshRepository;
//
//    public ReissueController(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
//        this.jwtUtil = jwtUtil;
//        this.refreshRepository = refreshRepository;
//    }
//
//    @PostMapping("/reissue")
//    @Operation(summary = "✅ 엑세스 토큰 재발급  ", description = "리프레시 토큰으로 엑세스 토큰 재발급")
//    public ResponseEntity<?> reissueProcess(HttpServletRequest request,
//                                            HttpServletResponse response) {
//
//        //get refresh token
//        String refresh = null;
//        Cookie[] cookies = request.getCookies();
//
//        for (Cookie cookie : cookies) {
//            log.trace("cookie name : {}",cookie.getName());
//            log.trace("cookie value : {}",cookie.getValue());
//            log.trace("refresh token category : {}",jwtUtil.getCategory(cookie.getValue()));
//            log.trace("refresh token role : {}",jwtUtil.getRole(cookie.getValue()));
//            log.trace("refresh token username : {}",jwtUtil.getUsername(cookie.getValue()));
//
//            if (cookie.getName().equals(refreshHeader)) {
//
//                refresh = cookie.getValue();
//            }
//        }
//
//        if (refresh == null) {
//
//            //response status code
//            return new ResponseEntity<>(refreshHeader + "이 null 입니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        //expired check
//        try {
//            jwtUtil.isExpired(refresh);
//        } catch (ExpiredJwtException e) {
//
//            //response status code
//            return new ResponseEntity<>(refreshHeader + " 이 만료 되었습니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
//        String category = jwtUtil.getCategory(refresh);
//
//        if (!category.equals(refreshHeader)) {
//
//            //response status code
//            return new ResponseEntity<>(refreshHeader +  "헤더명이 잘못 되었습니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        String username = jwtUtil.getUsername(refresh);
//        String role = jwtUtil.getRole(refresh);
//
//        //make new JWT
//        String newAccess = jwtUtil.createJwt(accessHeader, username, role, accessExpiration);
//
//        //response
//        response.setHeader(accessHeader, newAccess);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//}
//
////eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6IlJlZnJlc2gtVG9rZW4iLCJ1c2VybmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzE1Mzk3MTQ2LCJleHAiOjE3MTUzOTcxNDZ9._qGgq8shhHAMZEon1VguXv4f_he3jDOSK0N8gZIQOV0
////eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6IlJlZnJlc2gtVG9rZW4iLCJ1c2VybmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzE1Mzk3MTQ2LCJleHAiOjE3MTUzOTcxNDZ9._qGgq8shhHAMZEon1VguXv4f_he3jDOSK0N8gZIQOV0