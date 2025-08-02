package com.kin1st.coursemanagement.service;

import com.kin1st.coursemanagement.model.Course;
import com.kin1st.coursemanagement.model.User;

import java.util.List;

public interface ICourseService extends IGenerateService<Course> {
    List<Course> findByTeacherId(Long teacherId);
}
