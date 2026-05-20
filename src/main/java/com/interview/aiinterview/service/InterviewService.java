package com.interview.aiinterview.service;

import com.interview.aiinterview.model.InterviewResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Service
public class InterviewService {

    @Value("${gemini.api.key}")
    private String API_KEY;
   private final WebClient webClient = WebClient.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                .build();


    public InterviewResponse generateQuestions(String jobRole) {
        String prompt = String.format(
                "Generate exactly 5 technical interview questions for a %s position. " +
                        "Format strictly as numbered list:\n" +
                        "1. question\n2. question\n3. question\n4. question\n5. question\n" +
                        "Mix easy, medium and hard questions. " +
                        "DO NOT use markdown formatting like ** or * in the questions.", jobRole
        );
        return callGemini(prompt);
    }
    public InterviewResponse getFeedback(String question, String answer, String jobRole) {
        String prompt = String.format(
                "You are a strict interviewer for %s roles.\n" +
                        "Question: %s\n" +
                        "Candidate answer: %s\n\n" +
                        "IMPORTANT: If the answer is random text, gibberish, too short, or completely irrelevant to the question, " +
                        "respond with:\n" +
                        "✅ What was good:\nNothing - no valid answer was provided.\n" +
                        "❌ What was missing:\nA proper answer to the question.\n" +
                        "💡 Ideal answer would include:\n[give the correct answer here]\n" +
                        "⭐ Score: 0/10\n\n" +
                        "Otherwise give feedback in exactly this format:\n" +
                        "✅ What was good:\n" +
                        "❌ What was missing:\n" +
                        "💡 Ideal answer would include:\n" +
                        "⭐ Score: X/10",
                jobRole, question, answer
        );
        return callGemini(prompt);
    }


    private InterviewResponse callGemini(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    )
            );

            Map response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("generativelanguage.googleapis.com")
                            .path("/v1beta/models/gemini-2.5-flash:generateContent")
                            .queryParam("key", API_KEY)
                            .build())
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map> candidates = (List<Map>) response.get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            String text = (String) parts.get(0).get("text");

            return new InterviewResponse(text, true);

        } catch (Exception e) {
            e.printStackTrace();
            return new InterviewResponse("Error: " + e.getMessage(), false);
        }
    }
    }
