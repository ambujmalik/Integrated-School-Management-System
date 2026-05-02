package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.BlockEducationOfficer;

@Repository
public interface BlockOfficerRepository extends JpaRepository<BlockEducationOfficer, Long> {

	List<BlockEducationOfficer> findByDistrictOfficerId(Long districtId);
}
