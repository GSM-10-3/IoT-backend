package com.example.IoT.app.service;


import com.example.IoT.app.DTO.AuthResponse;
import com.example.IoT.app.DTO.DataGsmUserInfo;
import com.example.IoT.app.DTO.TokenResponse;
import com.example.IoT.app.DTO.UserInfoResponse;
import com.example.IoT.app.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${datagsm.oauth.token-url:https://oauth.authorization.datagsm.kr/v1/oauth/token}")
    private String tokenUrl;

    @Value("${datagsm.oauth.userinfo-url:https://oauth.resource.datagsm.kr/userinfo}")
    private String userInfoUrl;

    @Value("${datagsm.oauth.client-id}")
    private String clientId;

    @Value("${datagsm.oauth.redirect-uri}")
    private String redirectUri;

    /**
     * OAuth 콜백 처리: Code → Token → UserInfo → JWT 발급
     */
    @Transactional
    public AuthResponse processOAuthCallback(String code, String codeVerifier) throws Exception {
        // 1. Authorization Code와 code_verifier로 Access Token 교환
        TokenResponse tokenResponse = exchangeCodeForToken(code, codeVerifier);

        // 2. Access Token으로 사용자 데이터 조회
        DataGsmUserInfo userInfo = fetchUserInfo(tokenResponse.getAccessToken());

        // 3. DB에 사용자 데이터 저장/업데이트
        User user = userService.syncUser(userInfo);

        // 4. 자체 JWT 발급
        String jwt = jwtService.generateToken(user);

        // 5. 응답 생성
        return AuthResponse.builder()
                .token(jwt)
                .user(UserInfoResponse.from(user))
                .build();
    }

    /**
     * Authorization Code를 Access Token으로 교환 (PKCE 사용)
     */
    private TokenResponse exchangeCodeForToken(String code, String codeVerifier) throws Exception {
        // 요청 본문 생성 (PKCE: code_verifier 포함)
        Map<String, String> requestBody = Map.of(
                "grant_type", "authorization_code",
                "code", code,
                "client_id", clientId,
                "redirect_uri", redirectUri,
                "code_verifier", codeVerifier
        );

        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // HTTP 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .build();

        // 요청 전송
        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            log.error("Failed to exchange token: {}", response.body());
            throw new RuntimeException("Token exchange failed: " + response.body());
        }

        // 응답 파싱
        return objectMapper.readValue(response.body(), TokenResponse.class);
    }

    /**
     * Access Token으로 DataGSM 사용자 데이터 조회
     */
    private DataGsmUserInfo fetchUserInfo(String accessToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(userInfoUrl))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            log.error("Failed to fetch user info: {}", response.body());
            throw new RuntimeException("Failed to fetch user info: " + response.body());
        }

        return objectMapper.readValue(response.body(), DataGsmUserInfo.class);
    }

    /**
     * JWT에서 사용자 데이터 추출
     */
    public UserInfoResponse getUserFromToken(String token) {
        Long userId = jwtService.extractUserId(token);
        User user = userService.findById(userId);
        return UserInfoResponse.from(user);
    }

    /**
     * 토큰 무효화 (로그아웃)
     */
    public void invalidateToken(String token) {
        // JWT는 stateless하므로 블랙리스트에 추가하거나
        // Redis 같은 캐시에 만료 처리할 수 있습니다.
        // 여기서는 간단히 로그만 남깁니다.
        log.info("Token invalidated");
    }
}