package com.example.IoT.app.DTO;


import lombok.*;
// OAuth 콜백 요청
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthCallbackRequest {
    private String code;
    private String codeVerifier;
}
