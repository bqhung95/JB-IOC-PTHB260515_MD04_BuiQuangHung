package com.example.studentservice.service;

import com.example.studentservice.client.CourseClient;
import com.example.studentservice.dto.EnrollRequest;
import com.example.studentservice.entity.Student;
import com.example.studentservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseClient courseClient;   // Cách A: OpenFeign (đồng bộ)

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));
    }

    // ============ CÁCH A: OpenFeign (ĐỒNG BỘ) ============
    public Student enrollWithFeign(EnrollRequest request) {
        // 1. Kiểm tra course tồn tại & còn capacity
        Map<String, Object> course = courseClient.getCourse(request.getCourseId());
        Integer capacity = (Integer) course.get("capacity");
        if (capacity == null || capacity <= 0) {
            throw new RuntimeException("Khóa học đã đầy!");
        }

        // 2. Giảm capacity bên CourseService
        courseClient.decrementCapacity(request.getCourseId());

        // 3. Cập nhật student
        Student student = getStudentById(request.getStudentId());
        if (!student.getEnrolledCourses().contains(request.getCourseId())) {
            student.getEnrolledCourses().add(request.getCourseId());
            studentRepository.save(student);
        }
        return student;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
