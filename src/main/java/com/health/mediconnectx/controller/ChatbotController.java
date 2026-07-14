package com.health.mediconnectx.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    @Value("${groq.api.key:}")
    private String groqApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, Object> request) {
        try {
            String userMessage = (String) request.get("message");
            if (userMessage == null || userMessage.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Message cannot be empty"));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of(
                    "role", "system",
                    "content", "You are MediBot, a helpful AI health assistant for MediConnectX hospital management system. " +
                            "You help patients and doctors with general health questions, symptom guidance, appointment advice, " +
                            "and medical information. Always recommend consulting a real doctor for serious issues. " +
                            "Be concise, empathetic, and professional. Do not provide specific diagnoses."
            ));
            messages.add(Map.of("role", "user", "content", userMessage));

            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.1-8b-instant");
            body.put("messages", messages);
            body.put("max_tokens", 512);
            body.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions",
                    entity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, String> messageObj = (Map<String, String>) choice.get("message");
                    String aiReply = messageObj.get("content");
                    return ResponseEntity.ok(Map.of("reply", aiReply));
                }
            }

            return ResponseEntity.status(500).body(Map.of("error", "No response from AI"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Chatbot service error: " + e.getMessage())
            );
        }
    }
}
