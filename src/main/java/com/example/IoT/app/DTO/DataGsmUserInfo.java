package com.example.IoT.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DataGSM 사용자 데이터 (중첩 구조)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataGsmUserInfo {
    private Long id;
    private String email;
    private String role;
    private String status;
    private String objectType;
    // isStudent는 Deprecated이며 objectType == "STUDENT"의 파생값입니다. 신규 연동은 objectType을 사용하세요.
    private Boolean isStudent;
    private StudentInfo student;
    private TeacherInfo teacher;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentInfo {
        private Long id;
        private String name;
        private Integer grade;
        private Integer classNum;
        private Integer number;
        private String major;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherInfo {
        private Long id;
        private String name;
        private String email;
        private String department;
        private String description;
    }
}
