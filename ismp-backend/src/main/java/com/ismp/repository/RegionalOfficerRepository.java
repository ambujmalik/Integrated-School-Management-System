package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.RegionalOfficer;

@Repository
public interface RegionalOfficerRepository extends JpaRepository<RegionalOfficer, Long>{

	List<RegionalOfficer> findBySecretaryId(Long secretaryId);
}
