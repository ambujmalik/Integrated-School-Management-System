package com.ismp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.Secretary;

@Repository
public interface SecretaryRepository extends JpaRepository<Secretary, Long>{

}
