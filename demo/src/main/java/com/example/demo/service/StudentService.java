package com.example.demo.service;




import com.example.demo.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudents();
    Optional<Student> getStudentById(Long id);
    Student saveStudent(Student student);
    Student updateStudent(Long id, Student student);
    void deleteStudent(Long id);
    boolean studentExists(Long id);
    List<Student> searchStudentsByLastName(String lastName);
    boolean phoneNumberExists(String phoneNumber);
    boolean phoneNumberExistsForOtherStudent(String phoneNumber, Long id);
}
