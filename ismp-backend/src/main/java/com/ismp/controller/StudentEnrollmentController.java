package com.ismp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.StudentEnrollment;
import com.ismp.repository.StudentEnrollmentRepository;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentEnrollmentController {

	@Autowired
    private StudentEnrollmentRepository enrollmentRepository;

    @PostMapping
    public StudentEnrollment enrollStudent(@RequestBody StudentEnrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }
}
