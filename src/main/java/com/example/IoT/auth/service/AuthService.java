package com.example.IoT.auth.service;


import com.example.IoT.auth.domain.EmailVerification;
import com.example.IoT.auth.domain.UserEntity;
import com.example.IoT.auth.domain.UserRole;
import com.example.IoT.auth.repository.AuthRepository;
import com.example.IoT.auth.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthRepository authRepository;
    private final EmailRepository emailRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private static final String ALLOWED_DOMAIN = "@gsm.hs.kr";

    @Transactional
    public void signup(String username, String email, String password) {
        validateEmailDomain(email);
        EmailVerification verification =
                emailRepository.findTopByEmailOrderByCreatedAtDesc(email)
                        .orElseThrow(() ->
                                new IllegalArgumentException("이메일 인증을 먼저 해주세요.")
                        );

        if (verification.isUsed()){
            throw new IllegalArgumentException("이미 사용된 이메일입니다.");
        }
        if (!verification.isVerified()) {
            throw new IllegalArgumentException("이메일 인증을 먼저 해주세요.");
        }
        if (authRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 등록된 아이디입니다.");
        }
        if (authRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        if (password.length()<8){
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
//
        UserRole role = UserRole.USER;
        if ("admin".equals(username)) {  // 아이디가 admin인 경우 ADMIN 권한 부여
            role = UserRole.ADMIN;
        }

        String encodedPassword = passwordEncoder.encode(password);
        UserEntity user = new UserEntity(
                username,
                email,
                encodedPassword,
                role);
        verification.used();
        authRepository.save(user);
    }
    public void validateEmailDomain(String email) {
        if (email == null || !email.toLowerCase().endsWith(ALLOWED_DOMAIN)) {
            throw new IllegalArgumentException(
                    "허용되지 않은 이메일 도메인입니다."
            );
        }
    }

    public Authentication login(String email, String password) {
       return authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                    email,
                    password
            )
       );
    }
}
