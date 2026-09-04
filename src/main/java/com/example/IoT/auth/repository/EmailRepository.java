package com.example.IoT.auth.repository;

import com.example.IoT.auth.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);

    Optional<EmailVerification> findByEmail(String email);
}
