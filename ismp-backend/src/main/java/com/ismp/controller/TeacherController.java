package com.ismp.controller;

import java.util.List;

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

import com.ismp.model.Teacher;
import com.ismp.service.TeacherService;

/**
 * Teacher Controller
 * 
 * Manages all teacher-related REST API endpoints.
 * Provides CRUD operations for teachers including:
 * - Create new teachers
 * - Retrieve all teachers or a specific teacher by ID
 * - Update teacher information
 * - Delete teachers
 * 
 * CORS is managed centrally by SecurityConfig.
 * All endpoints require authentication as configured in SecurityConfig.
 * 
 * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

	private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

	@Autowired
	private TeacherService teacherService;

	/**
	 * Create a new teacher
	 * 
	 * @param teacher The teacher object containing teacher details
	 * @return ResponseEntity with created teacher and 201 (CREATED) status,
	 *         or 400 (BAD_REQUEST) if data is invalid
	 * 
	 * @example
	 * POST /api/teachers
	 * Body: { "firstName": "Jane", "lastName": "Smith", "email": "jane@school.com", "subject": "Math" }
	 * Response: 201 Created with teacher object
	 */
	@PostMapping
	public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
		try {
			if (teacher == null) {
				logger.warn("Attempt to create null teacher");
				return ResponseEntity.badRequest().build();
			}

			Teacher savedTeacher = teacherService.createTeacher(teacher);
			logger.info("Teacher created successfully with ID: {}", savedTeacher.getId());
			return new ResponseEntity<>(savedTeacher, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid teacher data provided: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error creating teacher", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve all teachers
	 * 
	 * @return ResponseEntity with list of all teachers and 200 (OK) status
	 * 
	 * @example
	 * GET /api/teachers
	 * Response: 200 OK with list of all teachers
	 */
	@GetMapping
	public ResponseEntity<List<Teacher>> getAllTeachers() {
		try {
			List<Teacher> teachers = teacherService.getAllTeachers();
			logger.info("Retrieved {} teachers", teachers.size());
			return new ResponseEntity<>(teachers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error retrieving all teachers", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve a specific teacher by ID
	 * 
	 * @param id The ID of the teacher (must be positive)
	 * @return ResponseEntity with teacher object and 200 (OK) status,
	 *         or 404 (NOT_FOUND) if teacher does not exist,
	 *         or 400 (BAD_REQUEST) if id is invalid
	 * 
	 * @example
	 * GET /api/teachers/1
	 * Response: 200 OK with teacher object
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable Integer id) {
		try {
			if (id == null || id <= 0) {
				logger.warn("Invalid teacher ID provided: {}", id);
				return ResponseEntity.badRequest().build();
			}

			Teacher teacher = teacherService.getTeacherById(id);
			if (teacher == null) {
				logger.info("Teacher not found with ID: {}", id);
				return ResponseEntity.notFound().build();
			}

			logger.info("Retrieved teacher with ID: {}", id);
			return ResponseEntity.ok(teacher);
		} catch (Exception e) {
			logger.error("Error retrieving teacher with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Update an existing teacher
	 * 
	 * @param id The ID of the teacher to update (must be positive)
	 * @param teacherDetails The updated teacher details
	 * @return ResponseEntity with updated teacher and 200 (OK) status,
	 *         or 404 (NOT_FOUND) if teacher does not exist,
	 *         or 400 (BAD_REQUEST) if data is invalid
	 * 
	 * @example
	 * PUT /api/teachers/1
	 * Body: { "firstName": "Janet", "lastName": "Johnson", "subject": "Science" }
	 * Response: 200 OK with updated teacher object
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Teacher> updateTeacher(@PathVariable Integer id, @RequestBody Teacher teacherDetails) {
		try {
			if (id == null || id <= 0) {
				logger.warn("Invalid teacher ID provided: {}", id);
				return ResponseEntity.badRequest().build();
			}

			if (teacherDetails == null) {
				logger.warn("Attempt to update teacher with null details");
				return ResponseEntity.badRequest().build();
			}

			Teacher updatedTeacher = teacherService.updateTeacher(id, teacherDetails);
			logger.info("Teacher updated successfully with ID: {}", id);
			return ResponseEntity.ok(updatedTeacher);
		} catch (IllegalArgumentException e) {
			logger.warn("Teacher not found with ID: {}", id);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Error updating teacher with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Delete a teacher
	 * 
	 * @param id The ID of the teacher to delete (must be positive)
	 * @return ResponseEntity with 204 (NO_CONTENT) status on successful deletion,
	 *         or 404 (NOT_FOUND) if teacher does not exist,
	 *         or 400 (BAD_REQUEST) if id is invalid
	 * 
	 * @example
	 * DELETE /api/teachers/1
	 * Response: 204 No Content
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTeacher(@PathVariable Integer id) {
		try {
			if (id == null || id <= 0) {
				logger.warn("Invalid teacher ID provided for deletion: {}", id);
				return ResponseEntity.badRequest().build();
			}

			teacherService.deleteTeacher(id);
			logger.info("Teacher deleted successfully with ID: {}", id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			logger.warn("Teacher not found with ID: {}", id);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Error deleting teacher with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
