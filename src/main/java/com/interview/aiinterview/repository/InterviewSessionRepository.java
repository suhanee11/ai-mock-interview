package com.interview.aiinterview.repository;


import com.interview.aiinterview.model.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface InterviewSessionRepository extends JpaRepository<InterviewSession,Long>{
    List<InterviewSession> findBySessionId(String sessionId);
}
