package com.vibe.auth.store;

import com.vibe.auth.model.UserDocument;
import com.vibe.auth.repo.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UserStore {
    private final UserRepository userRepository;

    public UserStore(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> createUser(Map<String, Object> payload) {
        String username = String.valueOf(payload.get("username"));
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("username");
        }
        if (!String.valueOf(payload.getOrDefault("password", "")).equals(payload.get("password_confirm"))) {
            throw new IllegalArgumentException("password_confirm");
        }

        UserDocument user = new UserDocument();
        user.setUsername(username);
        user.setPassword(String.valueOf(payload.get("password")));
        user.setEmail(String.valueOf(payload.getOrDefault("email", "")));
        user.setFirstName(String.valueOf(payload.getOrDefault("first_name", "")));
        user.setLastName(String.valueOf(payload.getOrDefault("last_name", "")));
        user.setPreferences(defaultPreferences());
        UserDocument saved = userRepository.save(user);
        return publicUser(internalUser(saved));
    }

    public Map<String, Object> login(String username, String password) {
        UserDocument user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !String.valueOf(user.getPassword()).equals(password)) {
            return null;
        }
        return internalUser(user);
    }

    public Map<String, Object> findById(String id) {
        UserDocument user = userRepository.findById(id).orElse(null);
        return user == null ? null : internalUser(user);
    }

    public Map<String, Object> publicUser(Map<String, Object> user) {
        Map<String, Object> pub = new HashMap<>();
        pub.put("id", user.get("id"));
        pub.put("username", user.get("username"));
        pub.put("email", user.get("email"));
        pub.put("first_name", user.get("first_name"));
        pub.put("last_name", user.get("last_name"));
        return pub;
    }

    public Map<String, Object> getPreferences(String userId) {
        UserDocument user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user"));
        if (user.getPreferences() == null || user.getPreferences().isEmpty()) {
            user.setPreferences(defaultPreferences());
            userRepository.save(user);
        }
        return user.getPreferences();
    }

    public Map<String, Object> updatePreferences(String userId, Map<String, Object> patch) {
        UserDocument user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user"));
        Map<String, Object> prefs = user.getPreferences();
        if (prefs == null) {
            prefs = defaultPreferences();
        }
        prefs.putAll(patch);
        user.setPreferences(prefs);
        userRepository.save(user);
        return prefs;
    }

    private Map<String, Object> internalUser(UserDocument user) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("password", user.getPassword());
        data.put("email", user.getEmail());
        data.put("first_name", user.getFirstName());
        data.put("last_name", user.getLastName());
        data.put("preferences", user.getPreferences());
        return data;
    }

    private Map<String, Object> defaultPreferences() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("theme", "auto");
        prefs.put("primary_color", "#4f46e5");
        prefs.put("sidebar_collapsed", false);
        prefs.put("default_report_period", "month");
        prefs.put("report_categories", List.of());
        prefs.put("report_include_charts", true);
        prefs.put("report_include_tables", true);
        prefs.put("report_email_frequency", "never");
        prefs.put("notify_budget_exceeded", true);
        prefs.put("notify_large_transaction", true);
        prefs.put("notify_anomaly_detected", true);
        prefs.put("large_transaction_threshold", 5000000);
        prefs.put("dashboard_widgets", List.of("summary", "cashflow", "categories"));
        prefs.put("dashboard_chart_type", "line");
        return prefs;
    }
}
