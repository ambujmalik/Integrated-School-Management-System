package com.ismp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.Exam;
import com.ismp.service.ExamService;

/**
 * Exam Controller
 * * Handles REST API endpoints for academic examination scheduling and management.
 * Provides functionality to:
 * - Create and schedule new examinations
 * - Retrieve a comprehensive list of all exams
 * * CORS is managed centrally by SecurityConfig, eliminating the need for @CrossOrigin.
 * All endpoints require authentication as configured in SecurityConfig.
 * * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/exams")
public class ExamController {

	private static final Logger logger = LoggerFactory.getLogger(ExamController.class);

	private final ExamService examService;

	// Modern constructor injection decoupling container boundaries for unit testing
	public ExamController(ExamService examService) {
		this.examService = examService;
	}

	/**
	 * Create a new examination schedule
	 * * @param exam The Exam details to save
	 * @return ResponseEntity with the created Exam details and 201 (CREATED) status,
	 * or 400 (BAD_REQUEST) if the schema payload properties are malformed
	 * * @example
	 * POST /api/exams
	 * Body: { "name": "Midterm Examination", "term": "Term 1", "totalMarks": 100 }
	 * Response: 201 Created with saved Exam object
	 */
	@PostMapping
	public ResponseEntity<Exam> createExam(@Valid @RequestBody Exam exam) {
		try {
			if (exam == null) {
				logger.warn("Attempted to register an empty or null Exam object");
				return ResponseEntity.badRequest().build();
			}

			Exam savedExam = examService.createExam(exam);
			logger.info("Examination record created successfully with ID: {}", savedExam.getId());
			return new ResponseEntity<>(savedExam, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Validation failed during exam registration: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Unexpected error encountered while creating exam entry", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve a list of all scheduled examinations
	 * * @return ResponseEntity with a list of all exams and 200 (OK) status,
	 * or 204 (NO_CONTENT) if no examinations exist in the system yet
	 * * @example
	 * GET /api/exams
	 * Response: 200 OK with list of all scheduled exams
	 */
	@GetMapping
	public ResponseEntity<List<Exam>> getAllExams() {
		try {
			List<Exam> exams = examService.getAllExams();

			if (exams == null || exams.isEmpty()) {
				logger.info("No examination records found within the tracking registry");
				return ResponseEntity.noContent().build(); // Restful status code for empty tables
			}

			logger.info("Retrieved {} active examination schedules", exams.size());
			return ResponseEntity.ok(exams);
		} catch (Exception e) {
			logger.error("Error occurred while querying the examination registry", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
