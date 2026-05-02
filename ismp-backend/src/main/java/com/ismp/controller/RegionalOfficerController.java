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

import com.ismp.model.RegionalOfficer;
import com.ismp.service.RegionalOfficerService;

@RestController
@RequestMapping("/api/regional-officers")
@CrossOrigin(origins = "http://localhost:3000")
public class RegionalOfficerController {

	@Autowired
    private RegionalOfficerService regionalOfficerService;

    @PostMapping
    public ResponseEntity<RegionalOfficer> create(@RequestBody RegionalOfficer officer) {
        return new ResponseEntity<>(regionalOfficerService.saveOfficer(officer), HttpStatus.CREATED);
    }

    @GetMapping("/secretary/{secretaryId}")
    public ResponseEntity<List<RegionalOfficer>> getBySecretary(@PathVariable Long secretaryId) {
        return ResponseEntity.ok(regionalOfficerService.getBySecretary(secretaryId));
    }
    
    @GetMapping
    public ResponseEntity<List<RegionalOfficer>> getAll() {
        return ResponseEntity.ok(regionalOfficerService.getAllOfficers());
    }
}
