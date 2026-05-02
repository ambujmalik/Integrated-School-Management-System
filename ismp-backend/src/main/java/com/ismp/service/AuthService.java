package com.ismp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ismp.model.User;
import com.ismp.repository.UserRepository;
import com.ismp.util.JwtUtil;

@Service
public class AuthService {

	@Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
     // 2. Check if user is active (since your entity has 'isActive')
        if (!user.getIsActive()) {
            throw new RuntimeException("This account is currently inactive.");
        }
        
        // Match against your 'passwordHash' field
        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            // Pass username and userRole to token generator
            return jwtUtil.generateToken(user.getUsername(), user.getUserRole());
        }
        throw new RuntimeException("Invalid credentials");
    }

    public User register(User user) {
        // Encode password and save into 'passwordHash'
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }
}
