package com.globallearning.platform.booking.repository;

import com.globallearning.platform.booking.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}