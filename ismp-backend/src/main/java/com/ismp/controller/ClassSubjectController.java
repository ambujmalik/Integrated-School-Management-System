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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.ClassSubject;
import com.ismp.service.ClassSubjectService;

/**
 * Class Subject Controller
 * * Handles REST API endpoints for managing subject allocations to academic classes.
 * Provides functionality to:
 * - Map / Assign a subject to a specific class
 * - Retrieve all subjects mapped to a specific class for a given academic year
 * * CORS is managed centrally by SecurityConfig, eliminating the need for @CrossOrigin.
 * All endpoints require authentication as configured in SecurityConfig.
 * * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/class-subjects")
public class ClassSubjectController {

	private static final Logger logger = LoggerFactory.getLogger(ClassSubjectController.class);

	private final ClassSubjectService classSubjectService;

	// Constructor injection preferred over field-level @Autowired
	public ClassSubjectController(ClassSubjectService classSubjectService) {
		this.classSubjectService = classSubjectService;
	}

	/**
	 * Assign a subject to a class
	 * * @param classSubject The mapping details between a class and a subject
	 * @return ResponseEntity with the created ClassSubject relationship and 201 (CREATED) status,
	 * or 400 (BAD_REQUEST) if input is invalid or creation fails
	 * * @example
	 * POST /api/class-subjects
	 * Body: { "classId": 3, "subjectId": 10, "year": "2026" }
	 * Response: 201 Created with saved ClassSubject object
	 */
	@PostMapping
	public ResponseEntity<ClassSubject> assignSubject(@Valid @RequestBody ClassSubject classSubject) {
		try {
			if (classSubject == null) {
				logger.warn("Attempted to assign a null ClassSubject object");
				return ResponseEntity.badRequest().build();
			}

			ClassSubject assignedSubject = classSubjectService.assignSubjectToClass(classSubject);
			logger.info("Subject successfully assigned to class. Relationship ID: {}", assignedSubject.getId());
			return new ResponseEntity<>(assignedSubject, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid class-subject mapping data provided: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error occurred while assigning subject to class", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve subjects for a specific class filtering by academic year
	 * * @param classId The ID of the class (must be positive)
	 * @param year    The academic year string (must not be blank)
	 * @return ResponseEntity with a list of mapped subjects and 200 (OK) status,
	 * or 204 (NO_CONTENT) if no subjects are mapped to the class for that year,
	 * or 400 (BAD_REQUEST) if parameters are malformed
	 * * @example
	 * GET /api/class-subjects/class/3?year=2026
	 * Response: 200 OK with list of subjects for class 3 in the year 2026
	 */
	@GetMapping("/class/{classId}")
	public ResponseEntity<List<ClassSubject>> getSubjects(@PathVariable Integer classId, @RequestParam String year) {
		try {
			if (classId == null || classId <= 0) {
				logger.warn("Invalid classId provided to fetch subjects: {}", classId);
				return ResponseEntity.badRequest().build();
			}
			if (year == null || year.trim().isEmpty()) {
				logger.warn("Blank academic year parameter provided for classId: {}", classId);
				return ResponseEntity.badRequest().build();
			}

			List<ClassSubject> subjects = classSubjectService.getSubjectsByClass(classId, year.trim());

			if (subjects == null || subjects.isEmpty()) {
				logger.info("No subjects found for classId: {} in academic year: {}", classId, year);
				return ResponseEntity.noContent().build(); // Restfully clean 204 No Content return code
			}

			logger.info("Retrieved {} subject(s) for classId: {} and year: {}", subjects.size(), classId, year);
			return ResponseEntity.ok(subjects);
		} catch (Exception e) {
			logger.error("Error retrieving subjects for classId: {} and year: {}", classId, year, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
