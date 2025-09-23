package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "success", required = false) String success,
                        Model model) {

        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
        }

        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы");
        }

        if (success != null) {
            model.addAttribute("message", "Регистрация прошла успешно! Теперь вы можете войти.");
        }

        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String confirmPassword,
            Model model) {

        // Проверка совпадения паролей
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        }

        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Имя пользователя уже существует");
            return "register";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Email уже зарегистрирован");
            return "register";
        }

        // Проверка длины пароля
        if (password.length() < 6) {
            model.addAttribute("error", "Пароль должен содержать не менее 6 символов");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        // Назначаем роль USER по умолчанию
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role("USER");
                    return roleRepository.save(newRole);
                });

        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);

        return "redirect:/login?success=true";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}