package utils;

import java.security.SecureRandom;
import java.util.Base64;

public class SessionTokenGenerator {

    public String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

//    public static void main(String[] args) {
//        String token = generateToken();
//        System.out.println("Session token: " + token);
//    }
}
