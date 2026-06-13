package com.interview.aiinterview.controller;

import com.interview.aiinterview.model.InterviewRequest;
import com.interview.aiinterview.model.InterviewResponse;
import com.interview.aiinterview.model.InterviewSession;
import com.interview.aiinterview.repository.InterviewSessionRepository;
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

    @Autowired
    private InterviewSessionRepository interviewSessionRepository;

    @PostMapping("/questions")
    public InterviewResponse generateQuestions(@RequestBody InterviewRequest request) {
        return interviewService.generateQuestions(request.getJobRole());
    }

    @PostMapping("/questions-from-resume")
    public InterviewResponse generateQuestionsFromResume(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobRole") String jobRole) {
        return interviewService.generateQuestionsFromResume(jobRole, resume);
    }

    @PostMapping("/feedback")
    public InterviewResponse getFeedback(@RequestBody InterviewRequest request) {
        InterviewResponse response = interviewService.getFeedback(
                request.getQuestion(),
                request.getJobRole(),
                request.getUserAnswer()
        );

        // Save to DB
        try {
            InterviewSession session = new InterviewSession();
            session.setSessionId(request.getSessionId() != null ? request.getSessionId() : "anonymous");
            session.setJobRole(request.getJobRole());
            session.setQuestion(request.getQuestion());
            session.setUserAnswer(request.getUserAnswer());
            session.setAiFeedback(response.getContent());
            session.setQuestionNumber(request.getQuestionNumber());
            session.setTimedOut(request.isTimedOut());
            interviewSessionRepository.save(session);
        } catch (Exception e) {
            System.out.println("Session save failed: " + e.getMessage());
        }

        return response;
    }
}