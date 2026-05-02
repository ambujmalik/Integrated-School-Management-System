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
    }

    @GetMapping("/district/{districtId}")
    public ResponseEntity<List<BlockEducationOfficer>> getByDistrict(@PathVariable Long districtId) {
        return ResponseEntity.ok(blockOfficerService.getByDistrict(districtId));
    }
}
