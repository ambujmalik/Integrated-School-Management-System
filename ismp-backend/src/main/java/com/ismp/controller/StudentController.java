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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ismp.model.Student;
import com.ismp.service.StudentService;

/**
 * Student Controller
 * 
 * Manages all student-related REST API endpoints.
 * Provides comprehensive CRUD operations for students including:
 * - Create individual or bulk students (via CSV upload)
 * - Retrieve students (all or filtered by class)
 * - Update student information
 * - Delete students
 * 
 * CORS is managed centrally by SecurityConfig.
 * All endpoints require authentication as configured in SecurityConfig.
 * 
 * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	/**
	 * Upload and create multiple students from CSV file
	 * 
	 * Supported file format: CSV with student data
	 * 
	 * @param file The CSV file containing student records
	 * @return ResponseEntity with success message and 201 (CREATED) status,
	 *         or 400 (BAD_REQUEST) if file is invalid/empty,
	 *         or 500 (INTERNAL_SERVER_ERROR) if processing fails
	 * 
	 * @example
	 * POST /api/students/upload
	 * Content-Type: multipart/form-data
	 * Body: file=students.csv
	 * Response: 201 Created with "Bulk upload successful"
	 */
	@PostMapping("/upload")
	public ResponseEntity<String> uploadStudents(@RequestParam("file") MultipartFile file) {
		try {
			if (file == null || file.isEmpty()) {
				logger.warn("Attempt to upload empty or null file");
				return ResponseEntity.badRequest().body("File is empty or not provided");
			}

			if (!file.getOriginalFilename().endsWith(".csv")) {
				logger.warn("Invalid file format uploaded: {}", file.getOriginalFilename());
				return ResponseEntity.badRequest().body("Only CSV files are supported");
			}

			studentService.saveBulkStudents(file);
			logger.info("Bulk upload successful for file: {}", file.getOriginalFilename());
			return ResponseEntity.status(HttpStatus.CREATED).body("Bulk upload successful");
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid CSV format: {}", e.getMessage());
			return ResponseEntity.badRequest().body("Invalid CSV format: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Error uploading bulk students", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	/**
	 * Create a new student
	 * 
	 * @param student The student object containing student details
	 * @return ResponseEntity with created student and 201 (CREATED) status,
	 *         or 400 (BAD_REQUEST) if data is invalid
	 * 
	 * @example
	 * POST /api/students
	 * Body: { "firstName": "John", "lastName": "Doe", "email": "john@school.com", "classId": 1 }
	 * Response: 201 Created with student object
	 */
	@PostMapping
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		try {
			if (student == null) {
				logger.warn("Attempt to create null student");
				return ResponseEntity.badRequest().build();
			}

			Student savedStudent = studentService.createStudent(student);
			logger.info("Student created successfully with ID: {}", savedStudent.getId());
			return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid student data provided: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error creating student", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve all students
	 * 
	 * @return ResponseEntity with list of all students and 200 (OK) status
	 * 
	 * @example
	 * GET /api/students
	 * Response: 200 OK with list of all students
	 */
	@GetMapping
	public ResponseEntity<List<Student>> getAllStudents() {
		try {
			List<Student> students = studentService.getAllStudents();
			logger.info("Retrieved {} students", students.size());
			return new ResponseEntity<>(students, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error retrieving all students", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Get students by class ID
	 * 
	 * @param classId The ID of the class (must be positive)
	 * @return ResponseEntity with list of students for the class and 200 (OK) status,
	 *         or 204 (NO_CONTENT) if no students exist in the class,
	 *         or 400 (BAD_REQUEST) if classId is invalid
	 * 
	 * @example
	 * GET /api/students/class/1
	 * Response: 200 OK with list of students in class 1
	 */
	@GetMapping("/class/{classId}")
	public ResponseEntity<List<Student>> getStudentsByClassId(@PathVariable Integer classId) {
		try {
			if (classId == null || classId <= 0) {
				logger.warn("Invalid classId provided: {}", classId);
				return ResponseEntity.badRequest().build();
			}

			List<Student> students = studentService.getStudentsByClass(classId);
			if (students.isEmpty()) {
				logger.info("No students found for classId: {}", classId);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			logger.info("Retrieved {} students for classId: {}", students.size(), classId);
			return new ResponseEntity<>(students, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error retrieving students for class: {}", classId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Update an existing student
	 * 
	 * @param id The ID of the student to update (must be positive)
	 * @param studentDetails The updated student details
	 * @return ResponseEntity with updated student and 200 (OK) status,
	 *         or 404 (NOT_FOUND) if student does not exist,
	 *         or 400 (BAD_REQUEST) if data is invalid
	 * 
	 * @example
	 * PUT /api/students/1
	 * Body: { "firstName": "Jane", "lastName": "Doe", "email": "jane@school.com" }
	 * Response: 200 OK with updated student object
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student studentDetails) {
		try {
			if (id == null || id <= 0) {
				logger.warn("Invalid student ID provided: {}", id);
				return ResponseEntity.badRequest().build();
			}

			if (studentDetails == null) {
				logger.warn("Attempt to update student with null details");
				return ResponseEntity.badRequest().build();
			}

			Student updatedStudent = studentService.updateStudent(id, studentDetails);
			logger.info("Student updated successfully with ID: {}", id);
			return ResponseEntity.ok(updatedStudent);
		} catch (IllegalArgumentException e) {
			logger.warn("Student not found with ID: {}", id);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Error updating student with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Delete a student
	 * 
	 * @param id The ID of the student to delete (must be positive)
	 * @return ResponseEntity with 204 (NO_CONTENT) status on successful deletion,
	 *         or 404 (NOT_FOUND) if student does not exist,
	 *         or 400 (BAD_REQUEST) if id is invalid
	 * 
	 * @example
	 * DELETE /api/students/1
	 * Response: 204 No Content
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
		try {
			if (id == null || id <= 0) {
				logger.warn("Invalid student ID provided for deletion: {}", id);
				return ResponseEntity.badRequest().build();
			}

			studentService.deleteStudent(id);
			logger.info("Student deleted successfully with ID: {}", id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			logger.warn("Student not found with ID: {}", id);
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error("Error deleting student with ID: {}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
