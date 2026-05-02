package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.FeeTransaction;
@Repository
public interface FeeTransactionRepository extends JpaRepository<FeeTransaction, Integer> {

	List<FeeTransaction> findByStudentStudentId(Integer studentId);
}
