package com.vibe.notification.service;

import java.time.LocalTime;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.vibe.notification.store.NotificationStore;

@Service
public class AnomalyService {
    private final EmailService emailService;
    private final WebClient webClient;
    private final NotificationStore notificationStore;

    public AnomalyService(EmailService emailService, WebClient.Builder webClientBuilder, NotificationStore notificationStore) {
        this.emailService = emailService;
        this.webClient = webClientBuilder.baseUrl("http://auth-service/auth").build();
        this.notificationStore = notificationStore;
    }

    public void checkAndNotify(String userId, Map<String, Object> transaction) {
        try {
            // Lấy thông tin người dùng từ auth-service
            Map<String, Object> user = webClient.get()
                .uri("/profile/")
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
                
            Map<String, Object> preferences = webClient.get()
                .uri("/preferences/")
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            if (user == null || preferences == null) return;

            String email = (String) user.get("email");
            if (email == null || email.isEmpty()) return;

            double amount = Double.parseDouble(String.valueOf(transaction.get("amount")));
            boolean isAnomaly = false;
            String reason = "";

            // 1. Kiểm tra giao dịch lớn
            boolean notifyLarge = (boolean) preferences.getOrDefault("notify_large_transaction", true);
            double threshold = Double.parseDouble(String.valueOf(preferences.getOrDefault("large_transaction_threshold", 5000000)));
            if (notifyLarge && amount >= threshold) {
                isAnomaly = true;
                reason += "- Giao dịch vượt ngưỡng " + threshold + "đ.\n";
            }

            // 2. Kiểm tra giờ lạ (1AM - 5AM)
            boolean notifyAnomaly = (boolean) preferences.getOrDefault("notify_anomaly_detected", true);
            LocalTime now = LocalTime.now();
            if (notifyAnomaly && (now.isAfter(LocalTime.of(1, 0)) && now.isBefore(LocalTime.of(5, 0)))) {
                isAnomaly = true;
                reason += "- Giao dịch phát sinh vào khung giờ lạ (1AM - 5AM).\n";
            }

            if (isAnomaly) {
                String subject = "[Vibe] Cảnh báo giao dịch bất thường";
                String content = String.format(
                    "Xin chào %s,\n\nHệ thống phát hiện giao dịch bất thường trên tài khoản của bạn:\n" +
                    "Số tiền: %,.0fđ\n" +
                    "Mô tả: %s\n" +
                    "Lý do cảnh báo:\n%s\n" +
                    "Nếu đây không phải là bạn, vui lòng kiểm tra lại tài khoản ngay lập tức.\n\nTrân trọng,\nVibe Team",
                    user.get("username"), amount, transaction.getOrDefault("description", "Không có mô tả"), reason
                );
                emailService.sendEmail(email, subject, content);
                
                // Lưu vào database để hiển thị trên UI với cấu trúc chi tiết
                java.util.Map<String, Object> metadata = new java.util.HashMap<>();
                metadata.put("amount", amount);
                metadata.put("category", transaction.getOrDefault("category_name", "Giao dịch"));
                metadata.put("deviation", 2.5); // Giả lập độ lệch chuẩn
                
                notificationStore.createNotification(
                    userId, 
                    "anomaly", 
                    "Phát hiện bất thường", 
                    String.format("Giao dịch %.0fđ: %s", amount, reason.replace("\n", " ")),
                    metadata
                );
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra bất thường: " + e.getMessage());
        }
    }
}
