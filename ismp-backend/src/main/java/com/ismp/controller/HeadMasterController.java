package com.ismp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.HeadMaster;
import com.ismp.service.HeadMasterService;

@RestController
@RequestMapping("/api/head-masters")
@CrossOrigin(origins = "http://localhost:3000")
public class HeadMasterController {

	@Autowired
    private HeadMasterService headMasterService;

    @PostMapping
    public ResponseEntity<HeadMaster> create(@RequestBody HeadMaster hm) {
        return new ResponseEntity<>(headMasterService.saveHeadMaster(hm), HttpStatus.CREATED);
    }

    @GetMapping("/block/{blockId}")
    public ResponseEntity<List<HeadMaster>> getByBlock(@PathVariable Long blockId) {
        return ResponseEntity.ok(headMasterService.getByBlockOfficer(blockId));
    }
}
