package com.ismp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.School;
import com.ismp.service.SchoolService;

@RestController
@RequestMapping("/api/schools")
@CrossOrigin(origins = "http://localhost:3000")
public class SchoolController {

	@Autowired
    private SchoolService schoolService;

    @PostMapping
    public ResponseEntity<School> createSchool(@RequestBody School school) {
        try {
            return new ResponseEntity<>(schoolService.createSchool(school), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<java.util.List<School>> getAllSchools() {
        return new ResponseEntity<>(schoolService.getAllSchools(), HttpStatus.OK);
    }
}
