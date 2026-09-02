package com.example.IoT.auth.service;


import com.example.IoT.auth.DTO.SignupRequest;
import com.example.IoT.auth.domain.EmailVerification;
import com.example.IoT.auth.domain.UserEntity;
import com.example.IoT.auth.domain.UserRole;
import com.example.IoT.auth.repository.AuthRepository;
import com.example.IoT.auth.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthRepository authRepository;
    private final EmailRepository emailRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final String ALLOWED_DOMAIN = "@gsm.hs.kr";


    public void signup(String username, String email, String password) {
        EmailVerification verification =
                emailRepository.findTopByEmailOrderByCreatedAtDesc(email)
                        .orElseThrow(() ->
                                new IllegalArgumentException("이메일 인증을 먼저 해주세요.")
                        );

        if (!verification.isVerified()) {
            throw new IllegalArgumentException("이메일 인증을 먼저 해주세요.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        UserEntity userEntity = new UserEntity(username, email, encodedPassword, UserRole.USER);
        authRepository.save(userEntity);
    }
    public void validateEmailDomain(String email) {
        if (email == null || !email.toLowerCase().endsWith(ALLOWED_DOMAIN)) {
            throw new IllegalArgumentException(
                    "허용되지 않은 이메일 도메인입니다."
            );
        }
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
