package com.ismp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.AssignmentSubmission;
import com.ismp.repository.AssignmentSubmissionRepository;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "http://localhost:3000")
public class AssignmentSubmissionController {

	@Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @PostMapping
    public AssignmentSubmission submitAssignment(@RequestBody AssignmentSubmission submission) {
        return submissionRepository.save(submission);
    }
}
