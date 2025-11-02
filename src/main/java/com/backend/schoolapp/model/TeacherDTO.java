package com.backend.schoolapp.model;

import lombok.Data;

@Data
public class TeacherDTO {
    private Long id;
    private String enrollment;
    private String firstName;
    private String lastName;
    private String email;
    private Long departmentId;
}