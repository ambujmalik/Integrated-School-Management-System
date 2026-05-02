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

import com.ismp.model.Assignment;
import com.ismp.service.AssignmentService;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "http://localhost:3000")
public class AssignmentController {

	@Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        try {
            Assignment savedAssignment = assignmentService.createAssignment(assignment);
            return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // This will print the exact error in Eclipse if it fails!
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return new ResponseEntity<>(assignmentService.getAllAssignments(), HttpStatus.OK);
    }
 // GET: Fetch assignments for a specific class ID
    // The {classId} in the URL becomes a variable we can use!
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByClassId(@PathVariable Integer classId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByClass(classId);
        
        // If the list is empty, return a 204 No Content, otherwise return the 200 OK
        if (assignments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }
    
}
