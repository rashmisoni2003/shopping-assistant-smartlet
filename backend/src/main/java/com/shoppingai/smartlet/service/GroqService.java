package com.shoppingai.smartlet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "gemma2-9b-it";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public String getGroqResponse(String prompt) throws Exception {
        String requestBody = """
            {
              "model": "%s",
              "messages": [{"role": "user", "content": "%s"}],
              "max_tokens": 150,
              "temperature": 0.7
            }
        """.formatted(MODEL, prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        System.out.println("Prompt: " + prompt);
        System.out.println("Request Body: " + requestBody);
        System.out.println("Groq API Key: " + apiKey);
        System.out.println("Response: " + body);

        // Extract assistant reply safely
        try {
            int contentIndex = body.indexOf("\"content\":\"");
            if (contentIndex == -1) return "[No reply received]";
            int start = contentIndex + 11;
            int end = body.indexOf("\"", start);
            if (end == -1) return "[Malformed response]";
            return body.substring(start, end).replace("\\n", "\n");
        } catch (Exception e) {
            return "[Error parsing response]";
        }
    }
}
