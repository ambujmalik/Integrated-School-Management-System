package com.ismp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.ismp.model.FeeStructure;
import com.ismp.model.FeeTransaction;
import com.ismp.repository.FeeStructureRepository;
import com.ismp.repository.FeeTransactionRepository;

@RestController
@RequestMapping("/api/fees")
@CrossOrigin(origins = "http://localhost:3000")
public class FeeController {

	@Autowired
    private FeeStructureRepository structureRepository;

    @Autowired
    private FeeTransactionRepository transactionRepository;

    // 1. Define the Fee for a Class
    @PostMapping("/structure")
    public FeeStructure createStructure(@RequestBody FeeStructure structure) {
        return structureRepository.save(structure);
    }

    // 2. Record a Student Payment
    @PostMapping("/pay")
    public FeeTransaction recordPayment(@RequestBody FeeTransaction transaction) {
        return transactionRepository.save(transaction);
    }

    // 3. Get Payment History for a Student
    @GetMapping("/history/{studentId}")
    public List<FeeTransaction> getHistory(@PathVariable Integer studentId) {
        return transactionRepository.findByStudentStudentId(studentId);
    }
}
