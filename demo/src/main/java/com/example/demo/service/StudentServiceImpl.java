package com.example.demo.service;



import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        student.setLastName(studentDetails.getLastName());
        student.setFirstName(studentDetails.getFirstName());
        student.setMiddleName(studentDetails.getMiddleName());
        student.setGender(studentDetails.getGender());
        student.setNationality(studentDetails.getNationality());
        student.setHeight(studentDetails.getHeight());
        student.setWeight(studentDetails.getWeight());
        student.setBirthDate(studentDetails.getBirthDate());
        student.setPhoneNumber(studentDetails.getPhoneNumber());
        student.setUniversity(studentDetails.getUniversity());
        student.setCourse(studentDetails.getCourse());
        student.setGroupName(studentDetails.getGroupName());
        student.setAverageGrade(studentDetails.getAverageGrade());
        student.setSpecialty(studentDetails.getSpecialty());

        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public boolean studentExists(Long id) {
        return studentRepository.existsById(id);
    }

    @Override
    public List<Student> searchStudentsByLastName(String lastName) {
        return studentRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    @Override
    public boolean phoneNumberExists(String phoneNumber) {
        return studentRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean phoneNumberExistsForOtherStudent(String phoneNumber, Long id) {
        return studentRepository.existsByPhoneNumberAndIdNot(phoneNumber, id);
    }
}
