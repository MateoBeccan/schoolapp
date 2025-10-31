package com.backend.schoolapp.repository;

import com.backend.schoolapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
