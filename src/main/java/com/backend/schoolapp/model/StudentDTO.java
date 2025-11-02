package com.backend.schoolapp.model;

import lombok.Data;
import java.util.List;

@Data
public class StudentDTO {
    private Long id;
    private String enrollment;
    private String firstName;
    private String lastName;
    private String email;
    private Long careerId;
    private List<Long> subjectIds;
}