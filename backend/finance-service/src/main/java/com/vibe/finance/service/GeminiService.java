package com.vibe.finance.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeminiService {
    private final WebClient webClient;
    
    @Value("${app.openrouter.api-key}")
    private String apiKey;
    
    @Value("${app.openrouter.model:gpt-oss-120b:free}") 
    private String model;

    public GeminiService(@org.springframework.beans.factory.annotation.Qualifier("externalWebClientBuilder") WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateResponse(String prompt) {
        String url = "https://openrouter.ai/api/v1/chat/completions";
        
        Map<String, Object> body = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "user", "content", prompt)
            ),
            "max_tokens", 1000
        );

        try {
            Map<String, Object> response = webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse -> 
                    clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                        System.err.println("OpenRouter API Error Body: " + errorBody);
                        return Mono.error(new RuntimeException("API Error: " + errorBody));
                    })
                )
                .bodyToMono(Map.class)
                .block();

            if (response != null && response.containsKey("choices")) {
                // Log token usage
                if (response.containsKey("usage")) {
                    Map<String, Object> usage = (Map<String, Object>) response.get("usage");
                    int promptTokens = ((Number) usage.getOrDefault("prompt_tokens", 0)).intValue();
                    int completionTokens = ((Number) usage.getOrDefault("completion_tokens", 0)).intValue();
                    int totalTokens = ((Number) usage.getOrDefault("total_tokens", 0)).intValue();
                    int cachedTokens = ((Number) usage.getOrDefault("prompt_cache_hit_tokens", 0)).intValue();
                    int remainingTokens = 1000 - completionTokens; // 1000 là max_tokens thiết lập ở trên

                    System.out.println("[OpenRouter Token Usage]");
                    System.out.println(" - Prompt: " + promptTokens + " (Cached: " + cachedTokens + ")");
                    System.out.println(" - Completion: " + completionTokens);
                    System.out.println(" - Total: " + totalTokens);
                    System.out.println(" - Unused (Remaining): " + remainingTokens);
                }
                
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    return (String) message.get("content");
                }
            }
            return "Xin lỗi, tôi không thể trả lời lúc này.";
        } catch (Exception e) {
            return "Lỗi khi kết nối với AI: " + e.getMessage();
        }
    }
}
