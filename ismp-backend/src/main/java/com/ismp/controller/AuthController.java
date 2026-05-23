package com.ismp.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.User;
import com.ismp.service.AuthService;

/**
 * Authentication Controller
 * 
 * Manages authentication and user registration endpoints.
 * Provides functionality to:
 * - Authenticate users and issue JWT tokens
 * - Register new users (teachers/admins)
 * 
 * CORS is managed centrally by SecurityConfig.
 * These endpoints are configured as public in SecurityConfig and do not require authentication.
 * 
 * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	/**
	 * User login endpoint
	 * 
	 * Authenticates a user with username and password, returns JWT token on success.
	 * 
	 * @param loginRequest Map containing "username" and "password" fields
	 * @return ResponseEntity with JWT token and 200 (OK) status,
	 *         or 401 (UNAUTHORIZED) if credentials are invalid
	 * 
	 * @example
	 * POST /api/auth/login
	 * Body: { "username": "teacher1", "password": "securePassword123" }
	 * Response: 200 OK with { "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
		try {
			if (loginRequest == null) {
				logger.warn("Null login request received");
				return buildErrorResponse("Login request cannot be null", HttpStatus.BAD_REQUEST);
			}

			String username = loginRequest.get("username");
			String password = loginRequest.get("password");

			if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
				logger.warn("Login attempt with missing credentials");
				return buildErrorResponse("Username and password are required", HttpStatus.BAD_REQUEST);
			}

			String token = authService.login(username, password);
			logger.info("User logged in successfully: {}", username);
			return ResponseEntity.ok(Map.of("token", token));
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid login credentials for user");
			return buildErrorResponse("Invalid username or password", HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			logger.error("Error during login", e);
			return buildErrorResponse("Authentication failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * User registration endpoint
	 * 
	 * Registers a new user (typically teacher or admin).
	 * 
	 * @param user The user object containing registration details
	 * @return ResponseEntity with created user and 201 (CREATED) status,
	 *         or 400 (BAD_REQUEST) if registration data is invalid
	 * 
	 * @example
	 * POST /api/auth/register
	 * Body: { "username": "newteacher", "email": "teacher@school.com", "password": "securePassword123", "role": "TEACHER" }
	 * Response: 201 Created with user object (password not included)
	 */
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			if (user == null) {
				logger.warn("Null user object received in registration");
				return buildErrorResponse("User data is required", HttpStatus.BAD_REQUEST);
			}

			if (user.getUsername() == null || user.getUsername().isEmpty()) {
				logger.warn("Registration attempt without username");
				return buildErrorResponse("Username is required", HttpStatus.BAD_REQUEST);
			}

			if (user.getEmail() == null || user.getEmail().isEmpty()) {
				logger.warn("Registration attempt without email for username: {}", user.getUsername());
				return buildErrorResponse("Email is required", HttpStatus.BAD_REQUEST);
			}

			User savedUser = authService.register(user);
			logger.info("New user registered successfully: {} with email: {}", savedUser.getUsername(), savedUser.getEmail());
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Registration validation error: {}", e.getMessage());
			return buildErrorResponse("Registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Error during user registration", e);
			return buildErrorResponse("Registration failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Builds a standardized error response
	 * 
	 * @param message The error message to include in the response
	 * @param status The HTTP status code
	 * @return ResponseEntity with error details
	 */
	private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", status.getReasonPhrase());
		errorResponse.put("message", message);
		return new ResponseEntity<>(errorResponse, status);
	}
}
