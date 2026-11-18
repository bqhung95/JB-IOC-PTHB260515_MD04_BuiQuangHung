package com.example.courseservice.service;

import com.example.courseservice.entity.Course;
import com.example.courseservice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    /** Giảm capacity khi có sinh viên đăng ký (Cách A) */
    public Course decrementCapacity(Long courseId) {
        Course course = getCourseById(courseId);
        if (course.getCapacity() <= 0) {
            throw new RuntimeException("Course is full: " + courseId);
        }
        course.setCapacity(course.getCapacity() - 1);
        return courseRepository.save(course);
    }
}
