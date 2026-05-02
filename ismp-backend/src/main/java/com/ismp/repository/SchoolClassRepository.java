package com.ismp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.SchoolClass;

@Repository
public interface SchoolClassRepository extends  JpaRepository<SchoolClass, Integer>{

}
