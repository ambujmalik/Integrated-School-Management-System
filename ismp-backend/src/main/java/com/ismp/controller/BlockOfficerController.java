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

import com.ismp.model.BlockEducationOfficer;
import com.ismp.service.BlockOfficerService;

/**
 * Block Officer Controller
 * Handles REST API endpoints for Block Education Officer (BEO) management.
 */
@RestController
@RequestMapping("/api/block-officers")
public class BlockOfficerController {

    private static final Logger logger = LoggerFactory.getLogger(BlockOfficerController.class);

    private final BlockOfficerService blockOfficerService;

    // Constructor injection
    public BlockOfficerController(BlockOfficerService blockOfficerService) {
        this.blockOfficerService = blockOfficerService;
    }

    /**
     * Create a new Block Education Officer (BEO)
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
                return ResponseEntity.noContent().build();
            }

            logger.info("Retrieved {} Block Education Officer(s) for districtId: {}", officers.size(), districtId);
            return ResponseEntity.ok(officers);
        } catch (Exception e) {
            logger.error("Error retrieving Block Education Officers for district: {}", districtId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
