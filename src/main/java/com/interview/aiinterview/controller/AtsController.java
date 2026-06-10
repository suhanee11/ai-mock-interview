package com.interview.aiinterview.controller;

import com.interview.aiinterview.service.AtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ats")
@CrossOrigin(origins = "http://localhost:5173")
public class AtsController {

    @Autowired
    private AtsService atsService;

    @PostMapping("/check")
    public ResponseEntity<String> checkAts(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobDescription") String jobDescription) {
        try {
            String result = atsService.checkAts(resume, jobDescription);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
