package com.ismp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.TeacherSubjectAllocation;
import com.ismp.repository.TeacherSubjectAllocationRepository;

@RestController
@RequestMapping("/api/allocations")
@CrossOrigin(origins = "http://localhost:3000")
public class TeacherSubjectAllocationController {

	@Autowired
    private TeacherSubjectAllocationRepository allocationRepository;

    @PostMapping
    public TeacherSubjectAllocation createAllocation(@RequestBody TeacherSubjectAllocation allocation) {
        return allocationRepository.save(allocation);
    }

    @GetMapping("/teacher/{teacherId}")
    public List<TeacherSubjectAllocation> getTeacherWorkload(@PathVariable Integer teacherId) {
        return allocationRepository.findByTeacherTeacherId(teacherId);
    }
}
