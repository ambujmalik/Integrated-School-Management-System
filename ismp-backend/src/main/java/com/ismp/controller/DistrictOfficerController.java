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

import com.ismp.model.DistrictOfficer;
import com.ismp.service.DistrictOfficerService;

@RestController
@RequestMapping("/api/district-officers")
@CrossOrigin(origins = "http://localhost:3000")
public class DistrictOfficerController {

	@Autowired
    private DistrictOfficerService districtOfficerService;

    @PostMapping
    public ResponseEntity<DistrictOfficer> create(@RequestBody DistrictOfficer officer) {
        return new ResponseEntity<>(districtOfficerService.saveOfficer(officer), HttpStatus.CREATED);
    }

    @GetMapping("/regional/{regionalId}")
    public ResponseEntity<List<DistrictOfficer>> getByRegional(@PathVariable Long regionalId) {
        return ResponseEntity.ok(districtOfficerService.getByRegionalOfficer(regionalId));
    }
}
