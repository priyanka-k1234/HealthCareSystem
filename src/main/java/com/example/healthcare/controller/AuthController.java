package com.example.healthcare.controller;

import com.example.healthcare.model.User;
import com.example.healthcare.service.UserService;
import com.example.healthcare.util.MfaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private MfaUtil mfaUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        User user = userOpt.get();
        if (!userService.checkPassword(user, password)) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        if (user.getMfaSecret() != null) {
            return ResponseEntity.ok("MFA_REQUIRED");
        }
        return ResponseEntity.ok("LOGIN_SUCCESS");
    }

    @PostMapping("/mfa-verify")
    public ResponseEntity<?> verifyMfa(@RequestParam String username, @RequestParam int code) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid user");
        }
        User user = userOpt.get();
        if (user.getMfaSecret() == null) {
            return ResponseEntity.badRequest().body("MFA not enabled");
        }
        boolean valid = mfaUtil.verifyCode(user.getMfaSecret(), code);
        if (!valid) {
            return ResponseEntity.status(401).body("Invalid MFA code");
        }
        return ResponseEntity.ok("MFA_SUCCESS");
    }
}