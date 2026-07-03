package com.example.benchmark.service;

import com.example.benchmark.entity.User;
import com.example.benchmark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Kullanıcı işlemlerini (Veritabanı veya Cache üzerinden getirme) yöneten servis katmanı.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Cache kullanmadan doğrudan veritabanından veri çeker. (Primary Key ile O(logN) hızında)
     */
    public User getUserFromDB(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * @Cacheable anotasyonu sayesinde verilen "id" key'i ile önce Redis'te veriyi arar.
     */
    @Cacheable(value = "users", key = "#id")
    public User getUserFromCache(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Gerçek veritabanı yavaşlığını görmek için indexsiz alanda ağır arama yapar.
     */
    public List<User> searchUsersFromDB(String emailPart) {
        return userRepository.findByEmailContaining(emailPart);
    }

    /**
     * Aynı ağır aramayı Redis Cache üzerinden yapar.
     */
    @Cacheable(value = "userSearch", key = "#emailPart")
    public List<User> searchUsersFromCache(String emailPart) {
        return userRepository.findByEmailContaining(emailPart);
    }
}
