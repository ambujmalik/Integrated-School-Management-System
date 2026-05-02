package com.ismp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismp.model.School;

public interface SchoolRepository extends JpaRepository<School, Integer> {

}
