package com.example.healthcare.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Component;

@Component
public class MfaUtil {
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key != null ? key.getKey() : null;
    }

    public boolean verifyCode(String secret, int code) {
        if (secret == null || secret.isEmpty()) return false;
        return gAuth.authorize(secret, code);
    }
}