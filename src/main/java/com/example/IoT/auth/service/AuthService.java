package com.example.IoT.auth.service;


import com.example.IoT.auth.DTO.SignupRequest;
import com.example.IoT.auth.domain.UserEntity;
import com.example.IoT.auth.domain.UserRole;
import com.example.IoT.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signup(String username, String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        UserEntity userEntity = new UserEntity(username, email, encodedPassword, UserRole.USER);
        authRepository.save(userEntity);
    }

    public void saveUser(SignupRequest signupRequest) {      // 유저 등록
        if (authRepository.existsByUsername(signupRequest.getUsername())) {
            throw new IllegalArgumentException("이미 등록된 아이디입니다.");
        }
        if (authRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
//        if (authRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())) {
//            throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
//        }
        UserRole role = UserRole.USER;
        if ("admin".equals(signupRequest.getUsername())) {  // 아이디가 admin인 경우 ADMIN 권한 부여
            role = UserRole.ADMIN;
        }
    }
}
