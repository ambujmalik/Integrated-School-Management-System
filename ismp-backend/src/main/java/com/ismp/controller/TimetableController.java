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

import com.ismp.model.Timetable;
import com.ismp.service.TimetableService;

@RestController
@RequestMapping("/api/timetables")
@CrossOrigin(origins = "http://localhost:3000")
public class TimetableController {

	@Autowired
    private TimetableService timetableService;

    @PostMapping
    public ResponseEntity<Timetable> saveTimetable(@RequestBody Timetable timetable) {
        return new ResponseEntity<>(timetableService.createFullTimetable(timetable), HttpStatus.CREATED);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Timetable>> getByClass(@PathVariable Integer classId) {
        return ResponseEntity.ok(timetableService.getActiveTimetableByClass(classId));
    }
}
