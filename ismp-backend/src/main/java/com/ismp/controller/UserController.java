package com.ismp.controller;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.User;
import com.ismp.service.UserService;

/**
 * User Controller
 * 
 * Manages all user-related REST API endpoints.
 * Provides CRUD operations for user management including:
 * - Create new users
 * - Retrieve users (all, by ID, or by email)
 * - Update user information
 * - Delete users
 * 
 * Includes email validation to ensure data integrity.
 * CORS is managed centrally by SecurityConfig.
 * All endpoints require authentication as configured in SecurityConfig.
 * 
 * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	@Autowired
	private UserService userService;

	/**
	 * Create a new user
	 * 
	 * Validates email format before creation.
	 * 
	 * @param user The user object containing user details
	 * @return ResponseEntity with created user and 201 (CREATED) status,
	 *         or 400 (BAD_REQUEST) if data is invalid or email already exists
	 * 
	 * @example
	 * POST /api/users
	 * Body: { "username": "johndoe", "email": "john@school.com", "role": "TEACHER" }
	 * Response: 201 Created with user object
	 */
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try {
			if (user == null) {
				logger.warn("Attempt to create null user");
				return ResponseEntity.badRequest().build();
			}

			if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
				logger.warn("Invalid email provided: {}", user.getEmail());
				return ResponseEntity.badRequest().build();
			}

			User savedUser = userService.createUser(user);
			logger.info("User created successfully with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("User creation failed - duplicate email or invalid data: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error creating user", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve all users
	 * 
	 * @return ResponseEntity with list of all users and 200 (OK) status
	 * 
	 * @example
	 * GET /api/users
	 * Response: 200 OK with list of all users
	 */
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> users = userService.getAllUsers();
			logger.info("Retrieved {} users", users.size());
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error retrieving all users", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve a specific user by ID
	 * 
	 * @param id The ID of the user (must be positive)
	 * @return ResponseEntity with user object and 200 (OK) status,
	 *         or 404 (NOT_FOUND) if user does not exist,
	 *         or 400 (BAD_REQUEST) if id is invalid
	 * 
	 * @example
	 * GET /api/users/1
	 * Response: 200 OK with user object
	 */
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Integer id) {
		try {
			if (id == null || id <= 0) {
				logger.warn("Invalid user ID provided: {}", id);
				return ResponseEntity.badRequest().build();
			}

			User user = userService.getUserById(id);
			if (user == null) {
				logger.info("User not found with ID: {}", id);
				return ResponseEntity.notFound().build();
			}

			logger.info("Retrieved user with ID: {}", id);
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			logger.error("Error retrieving user with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve a user by email address
	 * 
	 * @param email The email of the user
	 * @return ResponseEntity with user object and 200 (OK) status,
	 *         or 404 (NOT_FOUND) if user does not exist
	 * 
	 * @example
	 * GET /api/users/email/john@school.com
	 * Response: 200 OK with user object
	 */
	@GetMapping("/email/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		try {
			if (email == null || email.isEmpty()) {
				logger.warn("Empty email provided");
				return ResponseEntity.badRequest().build();
			}

			return userService.getUserByEmail(email)
				.map(user -> {
					logger.info("Retrieved user with email: {}", email);
					return new ResponseEntity<>(user, HttpStatus.OK);
				})
				.orElse({
					logger.info("User not found with email: {}", email);
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				});
		} catch (Exception e) {
			logger.error("Error retrieving user by email: {}", email, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Update an existing user
	 * 
	 * @param id The ID of the user to update (must be positive)
	 * @param userDetails The updated user details
	 * @return ResponseEntity with updated user and 200 (OK) status,
	 *         or 404 (NOT_FOUND) if user does not exist,
	 *         or 400 (BAD_REQUEST) if data is invalid
	 * 
	 * @example
	 * PUT /api/users/1
	 * Body: { "username": "janedoe", "email": "jane@school.com", "role": "ADMIN" }
	 * Response: 200 OK with updated user object
	 */
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
		try {
			if (id == null || id <= 0) {
				logger.warn("Invalid user ID provided: {}", id);
				return ResponseEntity.badRequest().build();
			}

			if (userDetails == null) {
				logger.warn("Attempt to update user with null details");
				return ResponseEntity.badRequest().build();
			}

			if (userDetails.getEmail() != null && !isValidEmail(userDetails.getEmail())) {
				logger.warn("Invalid email provided in update: {}", userDetails.getEmail());
				return ResponseEntity.badRequest().build();
			}

			User updatedUser = userService.updateUser(id, userDetails);
			logger.info("User updated successfully with ID: {}", id);
			return ResponseEntity.ok(updatedUser);
		} catch (IllegalArgumentException e) {
			logger.warn("User not found with ID: {}", id);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Error updating user with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Delete a user
	 * 
	 * @param id The ID of the user to delete (must be positive)
	 * @return ResponseEntity with 204 (NO_CONTENT) status on successful deletion,
	 *         or 404 (NOT_FOUND) if user does not exist,
	 *         or 400 (BAD_REQUEST) if id is invalid
	 * 
	 * @example
	 * DELETE /api/users/1
	 * Response: 204 No Content
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
		try {
			if (id == null || id <= 0) {
				logger.warn("Invalid user ID provided for deletion: {}", id);
				return ResponseEntity.badRequest().build();
			}

			userService.deleteUser(id);
			logger.info("User deleted successfully with ID: {}", id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			logger.warn("User not found with ID: {}", id);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Error deleting user with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Validates email format using regex pattern
	 * 
	 * @param email The email address to validate
	 * @return true if email format is valid, false otherwise
	 */
	private boolean isValidEmail(String email) {
		return email != null && pattern.matcher(email).matches();
	}
}
