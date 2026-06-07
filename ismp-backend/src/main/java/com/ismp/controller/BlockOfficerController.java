package com.ismp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.BlockEducationOfficer;
import com.ismp.service.BlockOfficerService;

@RestController
@RequestMapping("/api/block-officers")
@CrossOrigin(origins = "http://localhost:3000")
public class BlockOfficerController {

	@Autowired
    private BlockOfficerService blockOfficerService;

    @PostMapping
    public ResponseEntity<BlockEducationOfficer> create(@RequestBody BlockEducationOfficer beo) {
        return new ResponseEntity<>(blockOfficerService.saveBEO(beo), HttpStatus.CREATED);
    }package com.ismp.controller;

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

import com.ismp.model.BlockEducationOfficer;
import com.ismp.service.BlockOfficerService;

/**
 * Block Officer Controller
 * * Handles REST API endpoints for Block Education Officer (BEO) management.
 * Provides functionality to:
 * - Register/Create a new Block Education Officer
 * - Retrieve Block Education Officers filtered by their District ID
 * * CORS is managed centrally by SecurityConfig, eliminating the need for @CrossOrigin.
 * All endpoints require authentication as configured in SecurityConfig.
 * * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/block-officers")
public class BlockOfficerController {

	private static final Logger logger = LoggerFactory.getLogger(BlockOfficerController.class);

	private final BlockOfficerService blockOfficerService;

	// Constructor injection preferred over field-level @Autowired
	public BlockOfficerController(BlockOfficerService blockOfficerService) {
		this.blockOfficerService = blockOfficerService;
	}

	/**
	 * Create a new Block Education Officer (BEO)
	 * * @param beo The BlockEducationOfficer object containing registration details
	 * @return ResponseEntity with the created BEO and 201 (CREATED) status
	 * or 400 (BAD_REQUEST) if input is invalid or creation fails
	 * * @example
	 * POST /api/block-officers
	 * Body: { "name": "John Doe", "email": "beo.block@edu.gov", "districtId": 5 }
	 * Response: 201 Created with saved BlockEducationOfficer object
	 */
	@PostMapping
	public ResponseEntity<BlockEducationOfficer> create(@Valid @RequestBody BlockEducationOfficer beo) {
		try {
			if (beo == null) {
				logger.warn("Attempted to register a null Block Education Officer object");
				return ResponseEntity.badRequest().build();
			}

			BlockEducationOfficer savedBeo = blockOfficerService.saveBEO(beo);
			logger.info("Block Education Officer registered successfully with ID: {}", savedBeo.getId());
			return new ResponseEntity<>(savedBeo, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid BEO registration data provided: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error creating Block Education Officer record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Get all Block Education Officers for a specific district
	 * * @param districtId The ID of the district (must be positive)
	 * @return ResponseEntity with a list of BEOs for the district and 200 (OK) status,
	 * or 204 (NO_CONTENT) if no BEOs are allocated to the district,
	 * or 400 (BAD_REQUEST) if districtId is invalid
	 * * @example
	 * GET /api/block-officers/district/5
	 * Response: 200 OK with list of Block Education Officers for district 5
	 */
	@GetMapping("/district/{districtId}")
	public ResponseEntity<List<BlockEducationOfficer>> getByDistrict(@PathVariable Long districtId) {
		try {
			if (districtId == null || districtId <= 0) {
				logger.warn("Invalid districtId provided to query BEOs: {}", districtId);
				return ResponseEntity.badRequest().build();
			}

			List<BlockEducationOfficer> officers = blockOfficerService.getByDistrict(districtId);

			if (officers == null || officers.isEmpty()) {
				logger.info("No Block Education Officers found for districtId: {}", districtId);
				return ResponseEntity.noContent().build(); // Standard 204 No Content for empty lists
			}

			logger.info("Retrieved {} Block Education Officer(s) for districtId: {}", officers.size(), districtId);
			return ResponseEntity.ok(officers);
		} catch (Exception e) {
			logger.error("Error retrieving Block Education Officers for district: {}", districtId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}

    @GetMapping("/district/{districtId}")
    public ResponseEntity<List<BlockEducationOfficer>> getByDistrict(@PathVariable Long districtId) {
        return ResponseEntity.ok(blockOfficerService.getByDistrict(districtId));
    }
}
