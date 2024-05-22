package com.test.studyroomreservationsystem.service;

public interface TokenService {
    String createAccessToken(String username, String role);
    String createRefreshToken(String username);
}
