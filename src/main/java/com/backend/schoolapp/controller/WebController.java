package com.backend.schoolapp.controller;

import com.backend.schoolapp.model.Student;
import com.backend.schoolapp.model.Subject;
import com.backend.schoolapp.model.Teacher;
import com.backend.schoolapp.service.StudentService;
import com.backend.schoolapp.service.SubjectService;
import com.backend.schoolapp.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final StudentService studentService;

    public WebController(TeacherService teacherService, SubjectService subjectService, StudentService studentService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.studentService = studentService;
    }

    // Página principal
    @GetMapping("/")
    public String home(Model model) {
        long studentsCount = studentService.getAll().size();
        long teachersCount = teacherService.getAll().size();
        long subjectsCount = subjectService.getAll().size();

        model.addAttribute("studentsCount", studentsCount);
        model.addAttribute("teachersCount", teachersCount);
        model.addAttribute("subjectsCount", subjectsCount);

        return "index";
    }

    // Profesores
    @GetMapping("/teachers")
    public String teachers(Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("teacher", new Teacher());
        return "teachers";
    }

    @PostMapping("/teachers")
    public String addTeacher(@ModelAttribute Teacher teacher) {
        teacherService.save(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.delete(id);
        return "redirect:/teachers";
    }

    // Materias
    @GetMapping("/subjects")
    public String subjects(Model model) {
        model.addAttribute("subjects", subjectService.getAll());
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("subject", new Subject());
        return "subjects";
    }

    @PostMapping("/subjects")
    public String addSubject(@ModelAttribute Subject subject) {
        subjectService.save(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/subjects/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        subjectService.delete(id);
        return "redirect:/subjects";
    }

    // Estudiantes
    @GetMapping("/students")
    public String students(Model model) {
        model.addAttribute("students", studentService.getAll());
        model.addAttribute("subjects", subjectService.getAll());
        model.addAttribute("student", new Student());
        return "students";
    }

    @PostMapping("/students")
    public String addStudent(@ModelAttribute Student student) {
        studentService.save(student);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/students";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
