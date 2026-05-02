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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.ClassSubject;
import com.ismp.service.ClassSubjectService;

@RestController
@RequestMapping("/api/class-subjects")
@CrossOrigin(origins = "http://localhost:3000")
public class ClassSubjectController {

	@Autowired
    private ClassSubjectService classSubjectService;

    @PostMapping
    public ResponseEntity<ClassSubject> assignSubject(@RequestBody ClassSubject classSubject) {
        return new ResponseEntity<>(classSubjectService.assignSubjectToClass(classSubject), HttpStatus.CREATED);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<ClassSubject>> getSubjects(@PathVariable Integer classId, @RequestParam String year) {
        return ResponseEntity.ok(classSubjectService.getSubjectsByClass(classId, year));
    }
}
