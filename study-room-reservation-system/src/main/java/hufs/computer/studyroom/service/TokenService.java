package hufs.computer.studyroom.service;

public interface TokenService {
    String createAccessToken(String username, String role);
    String createRefreshToken(String username);
    String createPasswordResetToken(String email);
}
