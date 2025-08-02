package com.kin1st.coursemanagement.controller;

import com.kin1st.coursemanagement.model.Course;
import com.kin1st.coursemanagement.model.User;
import com.kin1st.coursemanagement.service.imp.CourseService;
import com.kin1st.coursemanagement.service.imp.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    // Phương thức này nhận đối tượng Authentication để kiểm tra quyền
    private boolean hasAuthority(Authentication authentication, String authorityName) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase(authorityName));
    }

    // Phương thức này trả về đối tượng User của bạn, lấy từ database
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            return null; // Not authenticated or anonymous user
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.findByUsername(userDetails.getUsername()).orElse(null);
    }


    @GetMapping
    public String listCourses(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            // Trường hợp này có thể xảy ra khi người dùng chưa đăng nhập,
            // nhưng Spring Security thường sẽ chuyển hướng về trang login trước.
            return "redirect:/auth/login";
        }

        List<Course> courses = (List<Course>) courseService.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("currentUser", currentUser);
        return "teacher/list";
    }

    @GetMapping("/detail/{id}")
    public String courseDetail(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course không tồn tại với id: " + id));
        model.addAttribute("course", course);
        return "teacher/course-detail";
    }

    @GetMapping("/add")
    public String addCourseForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = getCurrentUser();

        // Kiểm tra quyền bằng cách truyền đối tượng Authentication vào
        if (currentUser == null || !(hasAuthority(authentication, "TEACHER") || hasAuthority(authentication, "ADMIN"))) {
            return "redirect:/courses";
        }

        model.addAttribute("course", new Course());
        model.addAttribute("currentUser", currentUser);
        return "teacher/course/add";
    }

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute("course") Course course,
                            BindingResult bindingResult,
                            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = getCurrentUser();

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentUser", currentUser);
            return "teacher/course/add";
        }

        // Kiểm tra quyền bằng cách truyền đối tượng Authentication vào
        if (currentUser == null || !(hasAuthority(authentication, "TEACHER") || hasAuthority(authentication, "ADMIN"))) {
            return "redirect:/courses";
        }

        course.setTeacher(currentUser);
        courseService.save(course);
        return "redirect:/courses/my-courses";
    }

    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course không tồn tại với id: " + id));

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            return "redirect:/courses";
        }

        model.addAttribute("course", course);
        model.addAttribute("currentUser", currentUser);
        return "teacher/course/edit";
    }

    @PostMapping("/edit")
    public String editCourse(@ModelAttribute Course course) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Course existingCourse = courseService.findById(course.getId())
                .orElseThrow(() -> new IllegalArgumentException("Course không tồn tại với id: " + course.getId()));

        if (!existingCourse.getTeacher().getId().equals(currentUser.getId())) {
            return "redirect:/courses";
        }

        existingCourse.setTitle(course.getTitle());
        existingCourse.setDescription(course.getDescription());
        courseService.save(existingCourse);

        return "redirect:/courses/my-courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        Course course = courseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course không tồn tại với id: " + id));

        if (!course.getTeacher().getId().equals(currentUser.getId())) {
            return "redirect:/courses";
        }

        courseService.remove(id);
        return "redirect:/courses/my-courses";
    }

    @GetMapping("/my-courses")
    public String myCourses(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = getCurrentUser();

        // Kiểm tra quyền bằng cách truyền đối tượng Authentication vào
        if (currentUser == null || !(hasAuthority(authentication, "TEACHER") || hasAuthority(authentication, "ADMIN"))) {
            return "redirect:/courses";
        }

        List<Course> myCourses = courseService.findByTeacherId(currentUser.getId());
        model.addAttribute("courses", myCourses);
        model.addAttribute("currentUser", currentUser);
        return "teacher/course/my-courses";
    }
}