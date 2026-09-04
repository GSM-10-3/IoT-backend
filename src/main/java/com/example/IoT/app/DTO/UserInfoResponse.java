package com.example.IoT.app.DTO;


import com.example.IoT.app.domain.User;
import lombok.*;

// 사용자 데이터 응답
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String email;
    private String name;
    private Integer grade;
    private Integer classNum;
    private Integer number;
    private String profileImage;

    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .grade(user.getGrade())
                .classNum(user.getClassNum())
                .number(user.getNumber())
                .profileImage(user.getProfileImage())
                .build();
    }
}