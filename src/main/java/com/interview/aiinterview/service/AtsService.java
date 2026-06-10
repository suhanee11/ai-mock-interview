package com.interview.aiinterview.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Service
public class AtsService {

    @Value("${gemini.api.key}")
    private String API_KEY;

    private final WebClient webClient = WebClient.builder()
            .codecs(configurer ->
                    configurer.defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024))
            .build();

    public String checkAts(MultipartFile resumeFile, String jobDescription) throws Exception {
        // Extract text from PDF
        String resumeText;
        try (PDDocument doc = PDDocument.load(resumeFile.getInputStream())) {
            resumeText = new PDFTextStripper().getText(doc);
        }

        String prompt = String.format(
                "You are an ATS (Applicant Tracking System) expert.\n\n" +
                        "Resume:\n%s\n\n" +
                        "Job Description:\n%s\n\n" +
                        "Analyze the resume against the job description and respond ONLY with this JSON format, nothing else:\n" +
                        "{\n" +
                        "  \"atsScore\": <number 0-100>,\n" +
                        "  \"summary\": \"<one line summary>\",\n" +
                        "  \"matchedKeywords\": [\"keyword1\", \"keyword2\"],\n" +
                        "  \"missingKeywords\": [\"keyword1\", \"keyword2\"],\n" +
                        "  \"suggestions\": [\"suggestion1\", \"suggestion2\"]\n" +
                        "}",
                resumeText, jobDescription
        );

        return callGemini(prompt);
    }

    private String callGemini(String prompt) {
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
            return (String) parts.get(0).get("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}
