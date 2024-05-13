//package com.test.studyroomreservationsystem.security.impl;
//
//import com.test.studyroomreservationsystem.domain.entity.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class AuthenticationProviderImpl implements AuthenticationProvider {
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String loginId = authentication.getName();
//        String password = (String) authentication.getCredentials();
//        UserDetails userEntity = userDetailsService.loadUserByUsername(loginId);
//
//        // 비밀번호가 잘못되었다면
//        if(!passwordEncoder.matches(password, userEntity.getPassword())) {
//            throw new BadCredentialsException("\nInvalid Password" +
//                    "\npassword : " + password +
//                    "\nuserEntity.getPassword() : "+ userEntity.getPassword());
//        }
//
//        return new UsernamePasswordAuthenticationToken(userEntity,
//                null,
//                userEntity.getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}
