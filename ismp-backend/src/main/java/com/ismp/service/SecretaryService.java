package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.Secretary;
import com.ismp.repository.SecretaryRepository;

@Service
public class SecretaryService {

	@Autowired
    private SecretaryRepository secretaryRepository;

    public Secretary saveSecretary(Secretary secretary) {
        return secretaryRepository.save(secretary);
    }

    public List<Secretary> getAllSecretaries() {
        return secretaryRepository.findAll();
    }

    public Secretary getSecretaryById(Long id) {
        return secretaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Secretary not found with id: " + id));
    }
}
