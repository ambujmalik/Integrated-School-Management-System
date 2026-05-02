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

import com.ismp.model.Subject;
import com.ismp.service.SubjectService;



@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "http://localhost:3000")// We are adding this now so your frontend can talk to it easily later!
public class SubjectController {
	
	@Autowired
    private SubjectService subjectService;

	// 1. POST: Create a new subject
    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        Subject savedSubject = subjectService.createSubject(subject);
        return new ResponseEntity<>(savedSubject, HttpStatus.CREATED);
    }

    // 2. GET: Fetch all subjects
    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

	
	}


