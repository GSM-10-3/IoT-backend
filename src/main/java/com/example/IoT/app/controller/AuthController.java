package com.example.IoT.app.controller;


import com.example.IoT.app.DTO.AuthResponse;
import com.example.IoT.app.DTO.OAuthCallbackRequest;
import com.example.IoT.app.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthService oAuthService;

    /**
     * OAuth 콜백 처리 및 JWT 발급
     *
     * @param request Authorization Code와 code_verifier를 포함한 요청
     * @return JWT 토큰 및 사용자 데이터
     */
    @PostMapping("/oauth/callback")
    public ResponseEntity<AuthResponse> handleOAuthCallback(
            @RequestBody OAuthCallbackRequest request) {

        try {
            // Authorization Code와 code_verifier로 토큰 교환 및 사용자 데이터 조회
            AuthResponse response = oAuthService.processOAuthCallback(
                    request.getCode(),
                    request.getCodeVerifier()
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 현재 로그인한 사용자 데이터 조회
     *
     * @param authHeader Authorization 헤더 (Bearer 토큰)
     * @return 사용자 데이터
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (!authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Invalid token format");
            }

            String token = authHeader.substring(7);
            var user = oAuthService.getUserFromToken(token);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }

    /**
     * 로그아웃
     *
     * @param authHeader Authorization 헤더
     * @return 성공 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            oAuthService.invalidateToken(token);

            return ResponseEntity.ok().body("Logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
