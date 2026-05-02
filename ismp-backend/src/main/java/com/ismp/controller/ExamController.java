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

import com.ismp.model.Exam;
import com.ismp.repository.ExamRepository;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "http://localhost:3000")
public class ExamController {

	@Autowired
    private ExamRepository examRepository;

    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        return new ResponseEntity<>(examRepository.save(exam), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        return ResponseEntity.ok(examRepository.findAll());
    }
}
