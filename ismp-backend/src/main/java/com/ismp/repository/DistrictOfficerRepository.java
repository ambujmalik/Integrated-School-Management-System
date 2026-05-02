package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.DistrictOfficer;

@Repository
public interface DistrictOfficerRepository extends JpaRepository<DistrictOfficer, Long> {

	List<DistrictOfficer> findByRegionalOfficerId(Long regionalOfficerId);
}
