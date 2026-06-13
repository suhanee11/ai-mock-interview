package com.interview.aiinterview.repository;

import com.interview.aiinterview.model.AtsSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AtsSessionRepository extends JpaRepository<AtsSession,Long> {
    List<AtsSession> findBySessionId(String sessionId);

}
