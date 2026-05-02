package com.ismp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.StudentEnrollment;

@Repository
public interface StudentEnrollmentRepository  extends JpaRepository<StudentEnrollment, Integer>{

}
