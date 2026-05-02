package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.BlockEducationOfficer;
import com.ismp.repository.BlockOfficerRepository;

@Service
public class BlockOfficerService {

	@Autowired
    private BlockOfficerRepository blockOfficerRepository;

    public BlockEducationOfficer saveBEO(BlockEducationOfficer beo) {
        return blockOfficerRepository.save(beo);
    }

    public List<BlockEducationOfficer> getByDistrict(Long districtId) {
        return blockOfficerRepository.findByDistrictOfficerId(districtId);
    }
}
