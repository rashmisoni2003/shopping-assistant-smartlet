package com.shoppingai.smartlet.service;

import com.shoppingai.smartlet.dto.AuthResponse;
import com.shoppingai.smartlet.model.User;
import com.shoppingai.smartlet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        return new AuthResponse("Login successful", "mock-token");
    }

    public AuthResponse signUp(User user) {
        userRepository.save(user);
        return new AuthResponse("User created", null);
    }
}