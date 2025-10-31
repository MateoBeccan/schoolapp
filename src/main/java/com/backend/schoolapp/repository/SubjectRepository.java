package com.backend.schoolapp.repository;

import com.backend.schoolapp.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
