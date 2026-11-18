package com.example.courseservice.controller;


import com.example.courseservice.entity.Course;
import com.example.courseservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> findAll() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getById(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PostMapping
    public Course create(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PutMapping("/{courseId}/decrement")
    public Course decrement(@PathVariable Long courseId) {
        return courseService.decrementCapacity(courseId);
    }
}
