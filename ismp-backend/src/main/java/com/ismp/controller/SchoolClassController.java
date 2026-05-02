package com.ismp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.SchoolClass;
import com.ismp.service.SchoolClassService;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "http://localhost:3000")
public class SchoolClassController {

	@Autowired
    private SchoolClassService schoolClassService;

    // POST: Create a new class
    @PostMapping
    public ResponseEntity<SchoolClass> createClass(@RequestBody SchoolClass schoolClass) {
        try {
            SchoolClass savedClass = schoolClassService.createClass(schoolClass);
            return new ResponseEntity<>(savedClass, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
 // GET: Fetch all classes
    @GetMapping
    public ResponseEntity<List<SchoolClass>> getAllClasses() {
        return new ResponseEntity<>(schoolClassService.getAllClasses(), HttpStatus.OK);
    }
}
