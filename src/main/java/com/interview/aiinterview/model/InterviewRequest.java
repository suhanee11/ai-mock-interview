package com.interview.aiinterview.model;

import lombok.Data;

@Data
public class InterviewRequest {
    private String jobRole;
    private String question;
    private String userAnswer;
    private String sessionId;
    private int questionNumber;
    private boolean timedOut;
}
