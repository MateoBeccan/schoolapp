package com.backend.schoolapp.model;

import lombok.Data;

@Data
public class SubjectDTO {
    private Long id;
    private String name;
    private String description;
    private int credits;
    private Long teacherId;
}