package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.service.OTPCodeService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPServiceImpl implements OTPCodeService {

    @Override
    public String generateRandomString(int length) {
        String characters = "abcdefghjkmnopqrstuvwxyz23456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return builder.toString();
    }
}