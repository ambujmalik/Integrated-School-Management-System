package com.ismp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ismp.exception.AuthenticationException;
import com.ismp.exception.ResourceNotFoundException;
import com.ismp.exception.ValidationException;
import com.ismp.model.User;
import com.ismp.repository.UserRepository;
import com.ismp.util.JwtUtil;

/**
 * Service layer for Authentication operations.
 * Handles user login and registration with proper validation and error handling.
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final String USER_NOT_FOUND = "User not found with username: ";
    private static final String ACCOUNT_INACTIVE = "This account is currently inactive";
    private static final String INVALID_CREDENTIALS = "Invalid username or password";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param username the username
     * @param password the password
     * @return JWT token if authentication is successful
     * @throws ResourceNotFoundException if user is not found
     * @throws AuthenticationException if credentials are invalid or account is inactive
     * @throws ValidationException if username or password is empty
     */
    public String login(String username, String password) {
        validateLoginInput(username, password);

        logger.info("Login attempt for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", username);
                    return new ResourceNotFoundException(USER_NOT_FOUND + username);
                });

        // Check if user account is active
        if (!user.getIsActive()) {
            logger.warn("Login attempt on inactive account: {}", username);
            throw new AuthenticationException(ACCOUNT_INACTIVE);
        }

        // Validate password
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            logger.warn("Invalid password attempt for user: {}", username);
            throw new AuthenticationException(INVALID_CREDENTIALS);
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getUserRole());
        logger.info("User successfully authenticated: {}", username);

        return token;
    }

    /**
     * Registers a new user.
     *
     * @param user the user object to register
     * @return the registered user
     * @throws ValidationException if user data is invalid
     * @throws AuthenticationException if user already exists
     */
    public User register(User user) {
        validateRegistrationInput(user);

        logger.info("Registration attempt for user: {}", user.getUsername());

        // Check if user already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Registration failed: User already exists with username: {}", user.getUsername());
            throw new AuthenticationException("User already exists with this username");
        }

        // Encode password before saving
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        // Set default values
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }

        User registeredUser = userRepository.save(user);
        logger.info("User registered successfully: {}", user.getUsername());

        return registeredUser;
    }

    /**
     * Validates login input parameters.
     *
     * @param username the username
     * @param password the password
     * @throws ValidationException if input is invalid
     */
    private void validateLoginInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            logger.warn("Login attempted with empty username");
            throw new ValidationException("Username cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            logger.warn("Login attempted with empty password for user: {}", username);
            throw new ValidationException("Password cannot be empty");
        }

        if (username.length() > 100) {
            logger.warn("Login attempted with username exceeding max length");
            throw new ValidationException("Username is too long");
        }
    }

    /**
     * Validates registration input parameters.
     *
     * @param user the user object
     * @throws ValidationException if input is invalid
     */
    private void validateRegistrationInput(User user) {
        if (user == null) {
            throw new ValidationException("User object cannot be null");
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username is required");
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            throw new ValidationException("Password is required");
        }

        if (user.getUsername().length() > 100) {
            throw new ValidationException("Username must not exceed 100 characters");
        }

        if (user.getPasswordHash().length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long");
        }

        if (user.getUserRole() == null || user.getUserRole().trim().isEmpty()) {
            throw new ValidationException("User role is required");
        }
    }
}
