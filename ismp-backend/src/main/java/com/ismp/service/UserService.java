package com.ismp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.User;
import com.ismp.repository.UserRepository;

@Service
public class UserService {

	@Autowired
    private UserRepository userRepository;

    // 1. Create a new user
    public User createUser(User user) {
        // NOTE: In a real production app, we would encrypt the password here before saving!
        // We will add password encryption (BCrypt) in a later step to keep things simple right now.
        return userRepository.save(user);
    }

    // 2. Fetch all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 3. Fetch a specific user by their email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
