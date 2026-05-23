package com.ismp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.Assignment;
import com.ismp.service.AssignmentService;

/**
 * Assignment Controller
 * 
 * Handles REST API endpoints for assignment management.
 * Provides functionality to:
 * - Create new assignments
 * - Retrieve all assignments
 * - Query assignments by class ID
 * 
 * CORS is managed centrally by SecurityConfig, eliminating the need for @CrossOrigin.
 * All endpoints require authentication as configured in SecurityConfig.
 * 
 * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
	private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

	@Autowired
	private AssignmentService assignmentService;

	/**
	 * Create a new assignment
	 * 
	 * @param assignment The assignment object containing assignment details
	 * @return ResponseEntity with the created assignment and 201 (CREATED) status
	 *         or 400 (BAD_REQUEST) if input is invalid or creation fails
	 * 
	 * @example
	 * POST /api/assignments
	 * Body: { "title": "Math Assignment 1", "dueDate": "2024-06-30", "classId": 1 }
	 * Response: 201 Created with saved assignment object
	 */
	@PostMapping
	public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
		try {
			if (assignment == null) {
				logger.warn("Attempt to create null assignment");
				return ResponseEntity.badRequest().build();
			}

			Assignment savedAssignment = assignmentService.createAssignment(assignment);
			logger.info("Assignment created successfully with ID: {}", savedAssignment.getId());
			return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid assignment data provided: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error creating assignment", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve all assignments
	 * 
	 * @return ResponseEntity with list of all assignments and 200 (OK) status
	 * 
	 * @example
	 * GET /api/assignments
	 * Response: 200 OK with list of all assignments
	 */
	@GetMapping
	public ResponseEntity<List<Assignment>> getAllAssignments() {
		try {
			List<Assignment> assignments = assignmentService.getAllAssignments();
			logger.info("Retrieved {} assignments", assignments.size());
			return new ResponseEntity<>(assignments, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error retrieving all assignments", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Get assignments for a specific class
	 * 
	 * @param classId The ID of the class (must be positive)
	 * @return ResponseEntity with list of assignments for the class and 200 (OK) status,
	 *         or 204 (NO_CONTENT) if no assignments exist for the class,
	 *         or 400 (BAD_REQUEST) if classId is invalid
	 * 
	 * @example
	 * GET /api/assignments/class/1
	 * Response: 200 OK with list of assignments for class 1
	 */
	@GetMapping("/class/{classId}")
	public ResponseEntity<List<Assignment>> getAssignmentsByClassId(@PathVariable Integer classId) {
		try {
			if (classId == null || classId <= 0) {
				logger.warn("Invalid classId provided: {}", classId);
				return ResponseEntity.badRequest().build();
			}

			List<Assignment> assignments = assignmentService.getAssignmentsByClass(classId);

			if (assignments.isEmpty()) {
				logger.info("No assignments found for classId: {}", classId);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			logger.info("Retrieved {} assignments for classId: {}", assignments.size(), classId);
			return new ResponseEntity<>(assignments, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error retrieving assignments for class: {}", classId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
