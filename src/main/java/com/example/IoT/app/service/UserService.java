package com.example.IoT.app.service;

import com.example.IoT.app.DTO.DataGsmUserInfo;
import com.example.IoT.app.domain.User;
import com.example.IoT.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * DataGSM 사용자 데이터를 DB에 동기화
     *
     * - 신규 사용자: 생성
     * - 기존 사용자: 데이터 업데이트
     */
    @Transactional
    public User syncUser(DataGsmUserInfo userInfo) {
        return userRepository.findByEmail(userInfo.getEmail())
                .map(existingUser -> updateUser(existingUser, userInfo))
                .orElseGet(() -> createUser(userInfo));
    }

    /**
     * 신규 사용자 생성
     */
    private User createUser(DataGsmUserInfo userInfo) {
        DataGsmUserInfo.StudentInfo s = userInfo.getStudent();
        User user = User.builder()
                .email(userInfo.getEmail())
                .name(s != null ? s.getName() : "")
                .grade(s != null ? s.getGrade() : 0)
                .classNum(s != null ? s.getClassNum() : 0)
                .number(s != null ? s.getNumber() : 0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        log.info("Created new user: {}", saved.getEmail());
        return saved;
    }

    /**
     * 기존 사용자 데이터 업데이트
     */
    private User updateUser(User user, DataGsmUserInfo userInfo) {
        DataGsmUserInfo.StudentInfo s = userInfo.getStudent();
        if (s != null) {
            user.setName(s.getName());
            user.setGrade(s.getGrade());
            user.setClassNum(s.getClassNum());
            user.setNumber(s.getNumber());
        }
        user.setUpdatedAt(LocalDateTime.now());

        User updated = userRepository.save(user);
        log.info("Updated user: {}", updated.getEmail());
        return updated;
    }

    /**
     * ID로 사용자 조회
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    /**
     * 이메일로 사용자 조회
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}