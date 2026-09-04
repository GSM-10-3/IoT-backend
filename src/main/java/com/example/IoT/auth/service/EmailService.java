package com.example.IoT.auth.service;

import com.example.IoT.auth.domain.EmailVerification;
import com.example.IoT.auth.repository.AuthRepository;
import com.example.IoT.auth.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;
    private final AuthRepository authRepository;
    private final JavaMailSender mailSender;

    private final SecureRandom random = new SecureRandom();

    public void sendVerificationCode(String email){
        String code = String.format("%06d", random.nextInt(1_000_000));

        EmailVerification verification = new EmailVerification(email.trim(),
                code,
                Instant.now().plus(5, ChronoUnit.MINUTES),
                Instant.now());

        emailRepository.save(verification);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("이메일 인증번호");
        message.setText(
                "인증번호는 [" + code + "] 입니다. \n"
        );

        mailSender.send(message);
    }
    public void verifyCode(String email, String code) {

        EmailVerification emailVerification= emailRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("인증번호가 존재하지 않습니다.")
                );

        if (emailVerification.isVerified()) {
            throw new IllegalArgumentException("이미 인증된 이메일입니다.");
        }

        if (Instant.now().isAfter(emailVerification.getExpiresAt())) {
            throw new IllegalArgumentException("인증번호가 만료되었습니다.");
        }

        if (!emailVerification.getCode().equals(code)) {
            throw new IllegalArgumentException("인증번호가 올바르지 않습니다.");
        }

        // 이메일 인증 완료
        emailVerification.verify();
        emailRepository.save(emailVerification);
    }
    private static final String ALLOWED_DOMAIN = "@gsm.hs.kr";

    public void validateEmailDomain(String email) {
        if (email == null || !email.toLowerCase().endsWith(ALLOWED_DOMAIN)) {
            throw new IllegalArgumentException(
                    "허용되지 않은 이메일 도메인입니다."
            );
        }
    }
//    private void validateSchoolEmail(String email) {
//        String domain = appProperties.getSchoolEmailDomain().toLowerCase();
//        if (domain.isBlank()) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "학교 이메일 도메인이 설정되지 않았습니다.");
//        }
//        if (!email.endsWith(domain)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "학교 계정(" + domain + ")으로만 가입할 수 있습니다.");
//        }
//    }
}
