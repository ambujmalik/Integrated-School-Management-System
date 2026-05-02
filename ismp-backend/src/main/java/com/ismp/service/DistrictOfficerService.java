package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.DistrictOfficer;
import com.ismp.repository.DistrictOfficerRepository;

@Service
public class DistrictOfficerService {

	@Autowired
    private DistrictOfficerRepository districtOfficerRepository;

    public DistrictOfficer saveOfficer(DistrictOfficer officer) {
        return districtOfficerRepository.save(officer);
    }

    public List<DistrictOfficer> getByRegionalOfficer(Long regionalId) {
        return districtOfficerRepository.findByRegionalOfficerId(regionalId);
    }
}
