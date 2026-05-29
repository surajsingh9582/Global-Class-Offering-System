package com.globallearning.platform.booking.repository;

import com.globallearning.platform.booking.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
}
