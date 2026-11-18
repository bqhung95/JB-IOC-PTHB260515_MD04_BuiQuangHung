package com.example.studentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Map;

@FeignClient(name = "course-service")
public interface CourseClient {

    @GetMapping("/api/courses/{courseId}")
    Map<String, Object> getCourse(@PathVariable("courseId") Long courseId);

    @PutMapping("/api/courses/{courseId}/decrement")
    Map<String, Object> decrementCapacity(@PathVariable("courseId") Long courseId);
}
