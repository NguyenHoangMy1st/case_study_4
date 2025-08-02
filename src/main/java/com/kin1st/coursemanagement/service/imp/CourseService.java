package com.kin1st.coursemanagement.service.imp;

import com.kin1st.coursemanagement.model.Course;
import com.kin1st.coursemanagement.model.User;
import com.kin1st.coursemanagement.repository.ICourseRepository;
import com.kin1st.coursemanagement.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService implements ICourseService {
    @Autowired
    private ICourseRepository iCourseRepository;

    @Override
    public List<Course> findByTeacherId(Long teacherId) {
        return iCourseRepository.findByTeacher_Id(teacherId);
    }

    @Override
    public Iterable<Course> findAll() {
        return iCourseRepository.findAll();
    }

    @Override
    public Optional<Course> findById(Long id) {
        return iCourseRepository.findById(id);
    }

    @Override
    public Course save(Course course) {
        return iCourseRepository.save(course);
    }

    @Override
    public void remove(Long id) {
        iCourseRepository.deleteById(id);
    }
}
