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

import com.ismp.model.FeeStructure;
import com.ismp.model.FeeTransaction;
import com.ismp.repository.FeeStructureRepository;
import com.ismp.repository.FeeTransactionRepository;

/**
 * Fee Controller
 * 
 * Manages all fee-related REST API endpoints.
 * Provides functionality to:
 * - Define fee structures for classes
 * - Record student fee payments
 * - Retrieve payment history for students
 * 
 * CORS is managed centrally by SecurityConfig.
 * All endpoints require authentication as configured in SecurityConfig.
 * 
 * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/fees")
public class FeeController {

	private static final Logger logger = LoggerFactory.getLogger(FeeController.class);

	@Autowired
	private FeeStructureRepository structureRepository;

	@Autowired
	private FeeTransactionRepository transactionRepository;

	/**
	 * Define the fee structure for a class
	 * 
	 * Creates or updates the fee structure applicable to a specific class.
	 * 
	 * @param structure The fee structure object containing class ID and fee details
	 * @return ResponseEntity with created fee structure and 201 (CREATED) status,
	 *         or 400 (BAD_REQUEST) if data is invalid
	 * 
	 * @example
	 * POST /api/fees/structure
	 * Body: { "classId": 1, "amount": 5000, "currency": "INR", "frequency": "ANNUAL" }
	 * Response: 201 Created with fee structure object
	 */
	@PostMapping("/structure")
	public ResponseEntity<FeeStructure> createStructure(@RequestBody FeeStructure structure) {
		try {
			if (structure == null) {
				logger.warn("Attempt to create null fee structure");
				return ResponseEntity.badRequest().build();
			}

			FeeStructure savedStructure = structureRepository.save(structure);
			logger.info("Fee structure created successfully with ID: {} for classId: {}", 
				savedStructure.getId(), structure.getClassId());
			return new ResponseEntity<>(savedStructure, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid fee structure data provided: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error creating fee structure", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Record a student fee payment
	 * 
	 * Creates a fee transaction record when a student makes a payment.
	 * 
	 * @param transaction The fee transaction containing student ID, amount, and payment date
	 * @return ResponseEntity with created transaction and 201 (CREATED) status,
	 *         or 400 (BAD_REQUEST) if data is invalid
	 * 
	 * @example
	 * POST /api/fees/pay
	 * Body: { "studentId": 1, "amount": 5000, "paymentDate": "2024-06-20", "method": "CASH" }
	 * Response: 201 Created with transaction object
	 */
	@PostMapping("/pay")
	public ResponseEntity<FeeTransaction> recordPayment(@RequestBody FeeTransaction transaction) {
		try {
			if (transaction == null) {
				logger.warn("Attempt to record null fee transaction");
				return ResponseEntity.badRequest().build();
			}

			if (transaction.getAmount() == null || transaction.getAmount() <= 0) {
				logger.warn("Invalid transaction amount: {}", transaction.getAmount());
				return ResponseEntity.badRequest().build();
			}

			FeeTransaction savedTransaction = transactionRepository.save(transaction);
			logger.info("Fee payment recorded successfully with ID: {} for student ID: {}", 
				savedTransaction.getId(), transaction.getStudentId());
			return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid fee transaction data: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error recording fee payment", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Get payment history for a student
	 * 
	 * Retrieves all fee transactions for a specific student.
	 * 
	 * @param studentId The ID of the student (must be positive)
	 * @return ResponseEntity with list of transactions and 200 (OK) status,
	 *         or 204 (NO_CONTENT) if no payments exist,
	 *         or 400 (BAD_REQUEST) if studentId is invalid
	 * 
	 * @example
	 * GET /api/fees/history/1
	 * Response: 200 OK with list of all fee transactions for student 1
	 */
	@GetMapping("/history/{studentId}")
	public ResponseEntity<List<FeeTransaction>> getHistory(@PathVariable Integer studentId) {
		try {
			if (studentId == null || studentId <= 0) {
				logger.warn("Invalid studentId provided: {}", studentId);
				return ResponseEntity.badRequest().build();
			}

			List<FeeTransaction> history = transactionRepository.findByStudentStudentId(studentId);
			if (history.isEmpty()) {
				logger.info("No fee transactions found for student ID: {}", studentId);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			logger.info("Retrieved {} fee transactions for student ID: {}", history.size(), studentId);
			return ResponseEntity.ok(history);
		} catch (Exception e) {
			logger.error("Error retrieving fee history for student: {}", studentId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
