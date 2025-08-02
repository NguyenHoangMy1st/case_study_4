package com.kin1st.coursemanagement.repository;

import com.kin1st.coursemanagement.model.Course;
import com.kin1st.coursemanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher_Id(Long teacherId);
}