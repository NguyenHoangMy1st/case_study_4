package com.kin1st.coursemanagement.controller;

import com.kin1st.coursemanagement.model.Role;
import com.kin1st.coursemanagement.model.User;
import com.kin1st.coursemanagement.service.imp.RoleService;
import com.kin1st.coursemanagement.service.imp.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("TEACHER"))) {
                return "redirect:/teacher/dashboard";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("STUDENT"))) {
                return "redirect:/student/dashboard";
            }
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Đăng xuất thành công!");
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String name,
                           @RequestParam String phone,
                           @RequestParam String address,
                           @RequestParam Long roleId,
                           Model model) {

        if (userService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Tài khoản đã tồn tại");
            model.addAttribute("roles", roleService.findAll());
            return "auth/register";
        }

        Optional<Role> selectedRole = roleService.findById(roleId);
        if (selectedRole.isEmpty()) {
            model.addAttribute("error", "Role không hợp lệ");
            model.addAttribute("roles", roleService.findAll());
            return "auth/register";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setRoles(Set.of(selectedRole.get()));

        userService.save(newUser);

        return "redirect:/auth/login";
    }
}