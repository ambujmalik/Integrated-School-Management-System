package com.ismp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismp.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

	
}
