package com.example.IoT.auth.controller;


import com.example.IoT.auth.DTO.EmailSendRequest;
import com.example.IoT.auth.DTO.SignupRequest;
import com.example.IoT.auth.service.AuthService;
import com.example.IoT.auth.service.EmailService;
import com.example.IoT.auth.service.UserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeacherAuthController {
    private final AuthService authService;
    private final UserDetailsService UserDetailsService;
    private final EmailService emailService;

    @PostMapping("/email-send")
    public String email_send(@Valid @RequestBody EmailSendRequest emailSendRequest){
        authService.validateEmailDomain(emailSendRequest.email());
        emailService.sendVerificationCode(emailSendRequest.email());

        return "인증번호가 전송되었습니다.";

    }

    @PostMapping("/verify")
    public String verify(@RequestBody EmailSendRequest request){
        emailService.verifyCode(request.email(),
                request.code());
        return "인증되었습니다.";
    }


    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest signupRequest){

        emailService.validateEmailDomain(signupRequest.email());
        authService.signup(signupRequest.username(),
                signupRequest.email(),
                signupRequest.password());
        System.out.println("========== SIGNUP CONTROLLER ==========");
        return "회원가입 성공!";
    }
}
