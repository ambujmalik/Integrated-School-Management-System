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

import com.ismp.model.ExamResult;
import com.ismp.service.ExamResultService;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "http://localhost:3000")
public class ExamResultController {

	@Autowired
    private ExamResultService examResultService;

    @PostMapping
    public ResponseEntity<ExamResult> addResult(@RequestBody ExamResult result) {
        return new ResponseEntity<>(examResultService.saveResult(result), HttpStatus.CREATED);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ExamResult>> getStudentResults(@PathVariable Integer studentId) {
        return ResponseEntity.ok(examResultService.getResultsByStudent(studentId));
    }
}
