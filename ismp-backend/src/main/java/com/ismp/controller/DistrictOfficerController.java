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

import com.ismp.model.DistrictOfficer;
import com.ismp.service.DistrictOfficerService;

/**
 * District Officer Controller
 * * Handles REST API endpoints for District Officer management.
 * Provides functionality to:
 * - Onboard/Create new District Officers
 * - Retrieve District Officers filtered by a Regional Officer ID
 * * CORS is managed centrally by SecurityConfig, eliminating the need for @CrossOrigin.
 * All endpoints require authentication as configured in SecurityConfig.
 * * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/district-officers")
public class DistrictOfficerController {

	private static final Logger logger = LoggerFactory.getLogger(DistrictOfficerController.class);

	private final DistrictOfficerService districtOfficerService;

	// Constructor injection preferred over field-level @Autowired
	public DistrictOfficerController(DistrictOfficerService districtOfficerService) {
		this.districtOfficerService = districtOfficerService;
	}

	/**
	 * Onboard a new District Officer
	 * * @param officer The DistrictOfficer entity object holding onboarding details
	 * @return ResponseEntity with the created officer details and 201 (CREATED) status,
	 * or 400 (BAD_REQUEST) if payload is malformed or onboarding fails
	 * * @example
	 * POST /api/district-officers
	 * Body: { "name": "Jane Smith", "email": "do.district@edu.gov", "regionalId": 2 }
	 * Response: 201 Created with saved DistrictOfficer object
	 */
	@PostMapping
	public ResponseEntity<DistrictOfficer> create(@Valid @RequestBody DistrictOfficer officer) {
		try {
			if (officer == null) {
				logger.warn("Attempted to create a null District Officer record");
				return ResponseEntity.badRequest().build();
			}

			DistrictOfficer savedOfficer = districtOfficerService.saveOfficer(officer);
			logger.info("District Officer registered successfully with ID: {}", savedOfficer.getId());
			return new ResponseEntity<>(savedOfficer, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid District Officer registration data supplied: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("An error occurred while registering District Officer", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Retrieve all District Officers assigned under a specific Regional Officer
	 * * @param regionalId The ID of the regional officer (must be positive)
	 * @return ResponseEntity with a list of district officers and 200 (OK) status,
	 * or 204 (NO_CONTENT) if no district officers belong to the regional zone,
	 * or 400 (BAD_REQUEST) if regionalId parameter fails boundary check
	 * * @example
	 * GET /api/district-officers/regional/2
	 * Response: 200 OK with list of District Officers belonging to Regional Officer 2
	 */
	@GetMapping("/regional/{regionalId}")
	public ResponseEntity<List<DistrictOfficer>> getByRegional(@PathVariable Long regionalId) {
		try {
			if (regionalId == null || regionalId <= 0) {
				logger.warn("Invalid regionalId provided for officer lookup: {}", regionalId);
				return ResponseEntity.badRequest().build();
			}

			List<DistrictOfficer> officers = districtOfficerService.getByRegionalOfficer(regionalId);

			if (officers == null || officers.isEmpty()) {
				logger.info("No District Officers found linked to regionalId: {}", regionalId);
				return ResponseEntity.noContent().build(); // Restfully clean 204 Return status
			}

			logger.info("Retrieved {} District Officer(s) for regionalId: {}", officers.size(), regionalId);
			return ResponseEntity.ok(officers);
		} catch (Exception e) {
			logger.error("Error retrieving District Officers for regionalId: {}", regionalId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
