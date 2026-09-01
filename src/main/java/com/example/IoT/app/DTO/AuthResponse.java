package com.example.IoT.app.DTO;

import lombok.*;

// 인증 응답 (JWT + 사용자 데이터)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserInfoResponse user;
}
