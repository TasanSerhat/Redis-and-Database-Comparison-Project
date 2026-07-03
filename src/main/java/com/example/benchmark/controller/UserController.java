package com.example.benchmark.controller;

import com.example.benchmark.entity.User;
import com.example.benchmark.repository.UserRepository;
import com.example.benchmark.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Canlı performans testleri yapmak üzere JSON dönen Controller sınıfı.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            System.out.println("Gerçek veritabanı yükünü simüle etmek için 50.000 test verisi diske yazılıyor, lütfen bekleyin...");
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 50000; i++) {
                users.add(User.builder()
                        .name("User " + i)
                        .email("user" + i + "@example.com")
                        .build());
            }
            userRepository.saveAll(users);
            System.out.println("50.000 test verisi eklendi! Disk okuması için hazır.");
        }
    }

    // Yardımcı metot: JSON yanıtı oluşturmak için
    private Map<String, Object> buildResponse(String source, String type, long durationMs, String message, Object sampleData) {
        Map<String, Object> response = new HashMap<>();
        response.put("source", source);
        response.put("type", type);
        response.put("durationMs", durationMs);
        response.put("message", message);
        if (sampleData != null) {
            response.put("sampleData", sampleData);
        }
        return response;
    }

    // --- ESKİ PRIMARY KEY (ID) TESTLERİ ---

    @GetMapping("/db/{id}")
    public Map<String, Object> testDbPerformance(@PathVariable Long id) {
        long startTime = System.nanoTime();
        User user = null;
        for (int i = 0; i < 100; i++) {
            user = userService.getUserFromDB(id);
        }
        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;
        return buildResponse("db", "id", durationInMillis, "100 adet Doğrudan DB sorgusu süresi: " + durationInMillis + " ms", user);
    }

    @GetMapping("/cache/{id}")
    public Map<String, Object> testCachePerformance(@PathVariable Long id) {
        long startTime = System.nanoTime();
        User user = null;
        for (int i = 0; i < 100; i++) {
            user = userService.getUserFromCache(id);
        }
        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;
        return buildResponse("cache", "id", durationInMillis, "100 adet Redis Cache sorgusu süresi: " + durationInMillis + " ms", user);
    }

    // --- YENİ GERÇEK HAYAT "AĞIR" SORGULAMA TESTLERİ ---

    @GetMapping("/db-heavy/{emailPart}")
    public Map<String, Object> testHeavyDbPerformance(@PathVariable String emailPart) {
        long startTime = System.nanoTime();
        List<User> lastResult = null;
        for (int i = 0; i < 100; i++) {
            lastResult = userService.searchUsersFromDB(emailPart);
        }
        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;
        
        // Ekranda göstermek için dönen listenin sadece ilk elemanını alıyoruz ki UI şişmesin
        Object sample = (lastResult != null && !lastResult.isEmpty()) ? lastResult.get(0) : null;
        
        return buildResponse("db", "heavy", durationInMillis, "100 adet Ağır Veritabanı Sorgusu süresi: " + durationInMillis + " ms", sample);
    }

    @GetMapping("/cache-heavy/{emailPart}")
    public Map<String, Object> testHeavyCachePerformance(@PathVariable String emailPart) {
        long startTime = System.nanoTime();
        List<User> lastResult = null;
        for (int i = 0; i < 100; i++) {
            lastResult = userService.searchUsersFromCache(emailPart);
        }
        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;
        
        Object sample = (lastResult != null && !lastResult.isEmpty()) ? lastResult.get(0) : null;
        
        return buildResponse("cache", "heavy", durationInMillis, "100 adet Ağır Redis Cache Sorgusu süresi: " + durationInMillis + " ms", sample);
    }
}
