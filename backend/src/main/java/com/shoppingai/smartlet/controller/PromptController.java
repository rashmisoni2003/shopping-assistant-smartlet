package com.shoppingai.smartlet.controller;

import com.shoppingai.smartlet.service.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PromptController {

    @Autowired
    private GroqService groqService;

    @PostMapping("/prompt")
    public ResponseEntity<?> handlePrompt(@RequestBody Map<String, String> request) {
        try {

            String prompt = request.get("prompt");
            System.out.println("Received prompt: " + prompt);  // 🔍 Check what was sent
            String reply = groqService.getGroqResponse(prompt);
            System.out.println("Groq reply: " + reply);         // 🔍 Check the actual response
            return ResponseEntity.ok(Map.of("reply", reply));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to generate response"));
        }
    }
}
