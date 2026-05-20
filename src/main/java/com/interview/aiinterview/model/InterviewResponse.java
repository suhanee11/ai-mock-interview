package com.interview.aiinterview.model;


import lombok.Data;

@Data
public class InterviewResponse {
    private String content;
    private boolean success;

    public InterviewResponse(String content,boolean success){
        this.content=content;
        this.success=success;
    }

}
