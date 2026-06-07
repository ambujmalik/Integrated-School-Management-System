package com.ismp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.ExamResult;
import com.ismp.service.ExamResultService;

/**
 * Exam Result Controller
 * * Handles REST API endpoints for tracking and managing academic examination performance metrics.
 * Provides functionality to:
 * - Record and publish new examination result entries
 * - Retrieve a list of exam performance histories filtered by student ID
 * * CORS is managed centrally by SecurityConfig, eliminating the need for @CrossOrigin.
 * All endpoints require authentication as configured in SecurityConfig.
 * * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/results")
public class ExamResultController {

	private static final Logger logger = LoggerFactory.getLogger(ExamResultController.class);

	private final ExamResultService examResultService;

	// Constructor injection preferred over field-level @Autowired
	public ExamResultController(ExamResultService examResultService) {
		this.examResultService = examResultService;
	}

	/**
	 * Record a new examination result entry
	 * * @param result The ExamResult detail payload to persist
	 * @return ResponseEntity with the created ExamResult details and 201 (CREATED) status,
	 * or 400 (BAD_REQUEST) if schema parameters are invalid
	 * * @example
	 * POST /api/results
	 * Body: { "studentId": 101, "examId": 12, "marksObtained": 85.5, "grade": "A" }
	 * Response: 201 Created with saved ExamResult object
	 */
	@PostMapping
	public ResponseEntity<ExamResult> addResult(@Valid @RequestBody ExamResult result) {
		try {
			if (result == null) {
				logger.warn("Attempted to publish an empty or null ExamResult object");
				return ResponseEntity.badRequest().build();
			}

			ExamResult savedResult = examResultService.saveResult(result);
			logger.info("Examination score card published successfully with ID: {}", savedResult.getId());
			return new ResponseEntity<>(savedResult, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Validation constraint error during result submission: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Unexpected failure encountered while persisting examination scorecard", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Fetch all recorded examination results for a specific student
	 * * @param studentId The ID of the targeted student (must be positive)
	 * @return ResponseEntity with a list of exam scores and 200 (OK) status,
	 * or 204 (NO_CONTENT) if no exam marks exist for the student,
	 * or 400 (BAD_REQUEST) if studentId boundary verification fails
	 * * @example
	 * GET /api/results/student/101
	 * Response: 200 OK with list of all exam results registered under student 101
	 */
	@GetMapping("/student/{studentId}")
	public ResponseEntity<List<ExamResult>> getStudentResults(@PathVariable Integer studentId) {
		try {
			if (studentId == null || studentId <= 0) {
				logger.warn("Invalid studentId provided for performance query lookup: {}", studentId);
				return ResponseEntity.badRequest().build();
			}

			List<ExamResult> results = examResultService.getResultsByStudent(studentId);

			if (results == null || results.isEmpty()) {
				logger.info("No reported examination performance records found linked to studentId: {}", studentId);
				return ResponseEntity.noContent().build(); // Restfully clean 204 Return status
			}

			logger.info("Retrieved {} examination result records for studentId: {}", results.size(), studentId);
			return ResponseEntity.ok(results);
		} catch (Exception e) {
			logger.error("Error occurred while compiling examination record report for studentId: {}", studentId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
