package hufs.computer.studyroom.domain.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPServiceImpl implements OTPCodeService {
    @Value("${spring.service.randomChars}")
    private String randomChars;
    private final Random random = new Random();

    @Override
    public String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(randomChars.charAt(random.nextInt(randomChars.length())));
        }
        return builder.toString();
    }
}
