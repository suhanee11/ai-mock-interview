# AI Mock Interview — Backend

A Spring Boot backend for an AI-powered mock interview platform. Supports interview question generation, answer evaluation, and ATS resume analysis via PDF parsing.

## Tech Stack

- Java 17
- Spring Boot 4.0.6
- Spring WebMVC + WebFlux
- Apache PDFBox 2.0.29
- Lombok
- Maven

## Getting Started

```bash
git clone https://github.com/suhanee11/ai-mock-interview.git
cd ai-mock-interview
./mvnw spring-boot:run
```

Server runs at `http://localhost:8080`

## Features

- Generate AI-based interview questions by role/topic
- Generate questions based on uploaded resume
- Accept voice input from candidates for answers
- Evaluate and score candidate answers
- Upload PDF resume for ATS compatibility analysis

## Project Structure

```
src/main/java/com/interview/aiinterview/
├── controller/
│   ├── InterviewController.java
│   └── AtsController.java
├── model/                              # Data models (request/response POJOs)
│   ├── InterviewRequest.java
│   └── InterviewResponse.java
├── service/
│   ├── InterviewService.java
│   └── AtsService.java
└── AiInterviewApplication.java
```

## Frontend

[AI-Mock-Interview-Frontend](https://github.com/suhanee11/AI-Mock-Interview-Frontend) — React + Vite