package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.Assignment;
import com.ismp.repository.AssignmentRepository;

@Service
public class AssignmentService {

	@Autowired
    private AssignmentRepository assignmentRepository;

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
    
 // Fetch assignments for a specific class
    public List<Assignment> getAssignmentsByClass(Integer classId) {
        return assignmentRepository.findBySchoolClass_ClassId(classId);
    }
}
