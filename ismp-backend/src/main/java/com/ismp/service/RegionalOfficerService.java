package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.RegionalOfficer;
import com.ismp.repository.RegionalOfficerRepository;

@Service
public class RegionalOfficerService {

	@Autowired
    private RegionalOfficerRepository regionalOfficerRepository;

    public RegionalOfficer saveOfficer(RegionalOfficer officer) {
        return regionalOfficerRepository.save(officer);
    }

    public List<RegionalOfficer> getBySecretary(Long secretaryId) {
        return regionalOfficerRepository.findBySecretaryId(secretaryId);
    }
    
    public List<RegionalOfficer> getAllOfficers() {
        return regionalOfficerRepository.findAll();
    }
}
