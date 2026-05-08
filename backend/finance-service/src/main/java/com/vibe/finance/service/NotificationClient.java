package com.vibe.finance.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NotificationClient {
    private final WebClient webClient;

    public NotificationClient(@org.springframework.beans.factory.annotation.Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://notification-service/notifications").build();
    }

    public void checkAnomaly(String userId, Map<String, Object> transaction) {
        try {
            webClient.post()
                .uri("/check-anomaly/")
                .header("X-User-Id", userId)
                .bodyValue(transaction)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(); // Async call
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi yêu cầu kiểm tra bất thường: " + e.getMessage());
        }
    }
}
