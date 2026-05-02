package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.HeadMaster;

@Repository
public interface HeadMasterRepository extends JpaRepository<HeadMaster, Long>{

	List<HeadMaster> findByBlockOfficerId(Long blockOfficerId);
}
