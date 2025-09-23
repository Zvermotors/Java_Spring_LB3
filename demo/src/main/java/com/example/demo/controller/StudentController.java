package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/{id}")
    public String getStudentById(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        model.addAttribute("student", student);
        return "student-details";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String saveStudent(@ModelAttribute Student student,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "student-form";
        }

        if (studentService.phoneNumberExists(student.getPhoneNumber())) {
            result.rejectValue("phoneNumber", "error.student", "Номер телефона уже существует");
            return "student-form";
        }

        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("successMessage", "Студент успешно добавлен");
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        model.addAttribute("student", student);
        return "student-form";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute Student student,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "student-form";
        }

        if (studentService.phoneNumberExistsForOtherStudent(student.getPhoneNumber(), id)) {
            result.rejectValue("phoneNumber", "error.student", "Номер телефона уже используется другим студентом");
            return "student-form";
        }

        studentService.updateStudent(id, student);
        redirectAttributes.addFlashAttribute("successMessage", "Студент успешно обновлен");
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!studentService.studentExists(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Студент не найден");
            return "redirect:/students";
        }

        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Студент успешно удален");
        return "redirect:/students";
    }

    @GetMapping("/search")
    public String searchStudents(@RequestParam String lastName, Model model) {
        List<Student> students = studentService.searchStudentsByLastName(lastName);
        model.addAttribute("students", students);
        model.addAttribute("searchTerm", lastName);
        return "students";
    }
}