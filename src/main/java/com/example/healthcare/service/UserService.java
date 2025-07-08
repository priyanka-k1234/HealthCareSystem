package com.example.healthcare.service;

import com.example.healthcare.model.User;
import com.example.healthcare.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user, String rawPassword) {
        // --- Enforce non-null names ---
        if (user.getFirstName() == null || user.getLastName() == null) {
            throw new IllegalArgumentException("First name and last name must not be null");
        }
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPasswordHash(hashedPassword);
        return userRepository.save(user);
    }

    // Save user without changing password
    public User saveUserWithoutPassword(User user) {
        User existing = userRepository.findById(user.getId()).orElseThrow();
        user.setPasswordHash(existing.getPasswordHash());
        // --- Enforce non-null names ---
        if (user.getFirstName() == null || user.getLastName() == null) {
            throw new IllegalArgumentException("First name and last name must not be null");
        }
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    public String getFullNameById(Long id) {
        User user = getUserById(id);
        return user.getFirstName() + " " + user.getLastName();
    }
}