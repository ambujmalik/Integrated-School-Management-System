package com.ismp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.Parent;
import com.ismp.service.ParentService;

@RestController
@RequestMapping("/api/parents")
@CrossOrigin(origins = "http://localhost:3000")
public class ParentController {

	@Autowired
    private ParentService parentService;

    @PostMapping
    public ResponseEntity<Parent> createParent(@RequestBody Parent parent) {
        return ResponseEntity.ok(parentService.saveParent(parent));
    }

    @GetMapping
    public List<Parent> getAllParents() {
        return parentService.getAllParents();
    }
}
