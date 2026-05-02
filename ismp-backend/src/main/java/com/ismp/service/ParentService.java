package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.Parent;
import com.ismp.repository.ParentRepository;

@Service
public class ParentService {

	@Autowired
    private ParentRepository parentRepository;

    public Parent saveParent(Parent parent) {
        // Logic to verify user and student existence goes here
        return parentRepository.save(parent);
    }

    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }
}
