package com.ismp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.Secretary;
import com.ismp.service.SecretaryService;

@RestController
@RequestMapping("/api/secretaries")
@CrossOrigin(origins = "http://localhost:3000")
public class SecretaryController {

	@Autowired
    private SecretaryService secretaryService;

    @PostMapping
    public ResponseEntity<Secretary> createSecretary(@RequestBody Secretary secretary) {
        return new ResponseEntity<>(secretaryService.saveSecretary(secretary), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Secretary>> getAll() {
        return ResponseEntity.ok(secretaryService.getAllSecretaries());
    }
}
