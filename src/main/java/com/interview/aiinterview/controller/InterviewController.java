package com.interview.aiinterview.controller;

import com.interview.aiinterview.model.InterviewRequest;
import com.interview.aiinterview.model.InterviewResponse;
import com.interview.aiinterview.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = "http://localhost:5173")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @PostMapping("/questions")
    public InterviewResponse generateQuestions(@RequestBody InterviewRequest request) {
        return interviewService.generateQuestions(request.getJobRole());
    }

    // NEW — resume based questions
    @PostMapping("/questions-from-resume")
    public InterviewResponse generateQuestionsFromResume(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobRole") String jobRole) {
        return interviewService.generateQuestionsFromResume(jobRole, resume);
    }

    @PostMapping("/feedback")
    public InterviewResponse getFeedback(@RequestBody InterviewRequest request) {
        return interviewService.getFeedback(
                request.getQuestion(),
                request.getJobRole(),
                request.getUserAnswer()
        );
    }
}