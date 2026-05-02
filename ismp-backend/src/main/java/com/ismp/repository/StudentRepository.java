package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	// Magic Query: Find all students belonging to a specific class ID
  
	// Use this if you want to find students belonging to a specific school
    List<Student> findBySchool_SchoolId(Integer schoolId);

    // Use this if you want to find students by their current class integer
    List<Student> findByCurrentClass(Integer currentClass);
	
}
