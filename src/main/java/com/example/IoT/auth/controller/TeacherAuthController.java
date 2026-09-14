package com.example.IoT.auth.controller;


import com.example.IoT.auth.DTO.EmailSendRequest;
import com.example.IoT.auth.DTO.LoginRequest;
import com.example.IoT.auth.DTO.SignupRequest;
import com.example.IoT.auth.service.AuthService;
import com.example.IoT.auth.service.EmailService;
import com.example.IoT.auth.service.UserDetailsService;
import com.example.IoT.global.DTO.ApiResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeacherAuthController {
    private final AuthService authService;
    private final EmailService emailService;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/email-send")
    public ResponseEntity<?> email_send(@Valid @RequestBody EmailSendRequest emailSendRequest){
        try {
            authService.validateEmailDomain(emailSendRequest.email());
            emailService.sendVerificationCode(emailSendRequest.email());

            return ResponseEntity.ok(new ApiResponseDTO(200,
                    "인증번호가 발송되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponseDTO(400, e.getMessage())
            );
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody EmailSendRequest request){
        try {
            emailService.verifyCode(request.email(),
                    request.code());
            return ResponseEntity.ok(new ApiResponseDTO(200,
                    "인증되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponseDTO(400, e.getMessage())
            );
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
        try{
        authService.signup(
                signupRequest.username(),
                signupRequest.email(),
                signupRequest.password());
        return ResponseEntity.ok(new ApiResponseDTO(200,
                "회원가입이 성공적으로 완료되었습니다."));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(
                    new ApiResponseDTO(400, e.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse) {

        try{
            Authentication authentication =
                authService.login(loginRequest.email(),
                loginRequest.password());

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context,
                httpRequest,
                httpResponse
        );
        return ResponseEntity.ok(new ApiResponseDTO(
                200,
                "로그인이 성공적으로 완료되었습니다."));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(
                    new ApiResponseDTO(400, e.getMessage())
            );
        }
    }
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        return ResponseEntity.ok(
                Map.of(
                        "email", authentication.getName(),
                        "role", authentication.getAuthorities()
                                .stream()
                                .findFirst()
                                .map(GrantedAuthority::getAuthority)
                                .orElse("")
                )
        );
    }
}
