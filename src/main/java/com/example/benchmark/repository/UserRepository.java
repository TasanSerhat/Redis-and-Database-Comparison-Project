package com.example.benchmark.repository;

import com.example.benchmark.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User nesnesi için veritabanı işlemlerini yapacak JPA repository arayüzü.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Indexsiz bir alanda arama yaparak veritabanını Full Table Scan (Tüm Tabloyu Tarama) yapmaya zorlar
    List<User> findByEmailContaining(String emailPart);
}
