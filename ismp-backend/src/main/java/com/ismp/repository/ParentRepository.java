package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Integer> {

	List<Parent> findByStudentStudentId(Integer studentId);
}
