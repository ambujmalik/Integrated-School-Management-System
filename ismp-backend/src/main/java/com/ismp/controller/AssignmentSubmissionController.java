package com.ismp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.AssignmentSubmission;
import com.ismp.service.AssignmentSubmissionService;

/**
 * Assignment Submission Controller
 * * Handles REST API endpoints for student assignment submissions.
 * Provides functionality to:
 * - Submit a new student assignment
 * * CORS and authentication are managed centrally via security configurations.
 * * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/submissions")
public class AssignmentSubmissionController {

	private static final Logger logger = LoggerFactory.getLogger(AssignmentSubmissionController.class);

	private final AssignmentSubmissionService submissionService;

	// Constructor injection preferred over field-level @Autowired
	public AssignmentSubmissionController(AssignmentSubmissionService submissionService) {
		this.submissionService = submissionService;
	}

	/**
	 * Submit a new student assignment
	 * * @param submission The assignment submission details
	 * @return ResponseEntity with the created submission and 201 (CREATED) status,
	 * or 400 (BAD_REQUEST) if the payload is malformed
	 * * @example
	 * POST /api/submissions
	 * Body: { "assignmentId": 12, "studentId": 45, "submissionUrl": "https://github.com/..." }
	 * Response: 201 Created with saved submission data
	 */
	@PostMapping
	public ResponseEntity<AssignmentSubmission> submitAssignment(@Valid @RequestBody AssignmentSubmission submission) {
		try {
			if (submission == null) {
				logger.warn("Attempted to process a null assignment submission");
				return ResponseEntity.badRequest().build();
			}

			AssignmentSubmission savedSubmission = submissionService.saveSubmission(submission);
			logger.info("Assignment submitted successfully with ID: {}", savedSubmission.getId());
			
			return new ResponseEntity<>(savedSubmission, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid submission details provided: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Unexpected error encountered during assignment submission", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
