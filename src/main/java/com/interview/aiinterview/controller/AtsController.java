package com.interview.aiinterview.controller;

import com.interview.aiinterview.model.AtsSession;
import com.interview.aiinterview.repository.AtsSessionRepository;
import com.interview.aiinterview.service.AtsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private AtsSessionRepository atsSessionRepository;

    @PostMapping("/check")
    public ResponseEntity<String> checkAts(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam(value = "sessionId", required = false) String sessionId) {
        try {
            String result = atsService.checkAts(resume, jobDescription);

            // Save to DB
            try {
                String clean = result.replace("```json", "").replace("```", "").trim();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(clean);

                AtsSession session = new AtsSession();
                session.setSessionId(sessionId != null ? sessionId : "anonymous");
                session.setAtsScore(json.get("atsScore").asInt());
                session.setSummary(json.get("summary").asText());
                session.setMatchedKeywords(json.get("matchedKeywords").toString());
                session.setMissingKeywords(json.get("missingKeywords").toString());
                session.setSuggestions(json.get("suggestions").toString());
                atsSessionRepository.save(session);
            } catch (Exception ex) {
                System.out.println("Session save failed: " + ex.getMessage());
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}