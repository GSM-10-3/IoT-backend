package com.example.IoT.account.controller;

import com.example.IoT.account.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AuthService authService;

    public AccountController(AuthService authService) {
        this.authService = authService;
    }

//    @PostMapping("/signup")
//    public void signup(@RequestBody ){
//
//    }
//    @PostMapping("/login")
//    public void login(@RequestBody){}

//    @PostMapping("/email-send")
//
//    @PostMapping("/verify")
//
//    @PostMapping("/auth/findId")
//
//    @PostMapping("/auth/password-find")
//
//    @DeleteMapping("/delete-account")
//
//    @PatchMapping("/auth/password-change")
//
//    @PostMapping("/auth/logout")

}
