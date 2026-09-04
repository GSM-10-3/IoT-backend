package com.example.IoT.auth.repository;

import com.example.IoT.auth.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
