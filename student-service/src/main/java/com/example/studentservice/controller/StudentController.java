package com.example.studentservice.controller;

import com.example.studentservice.dto.EnrollRequest;
import com.example.studentservice.entity.Student;
import com.example.studentservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    /**
     * Cách A: Đồng bộ - OpenFeign
     */
    @PostMapping("/enroll")
    public Student enrollFeign(@RequestBody EnrollRequest request) {
        return studentService.enrollWithFeign(request);
    }
}
