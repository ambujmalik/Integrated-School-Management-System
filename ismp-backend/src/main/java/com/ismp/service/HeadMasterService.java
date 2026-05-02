package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.HeadMaster;
import com.ismp.repository.HeadMasterRepository;

@Service
public class HeadMasterService {

	@Autowired
    private HeadMasterRepository headMasterRepository;

    public HeadMaster saveHeadMaster(HeadMaster hm) {
        return headMasterRepository.save(hm);
    }

    public List<HeadMaster> getByBlockOfficer(Long blockId) {
        return headMasterRepository.findByBlockOfficerId(blockId);
    }
}
