package com.example.demo.Service.implimentations;

import com.example.demo.Dto.AddStudentDto;
import com.example.demo.Dto.Studentdto;
import com.example.demo.Entity.Student;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Service.StudentService;
import com.example.demo.exception.DuplicateResouseException;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImplementation implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Studentdto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(student -> modelMapper.map(student, Studentdto.class))
                .toList();
    }

    @Override
    public Studentdto getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No record found with ID: " + id));

        return modelMapper.map(student, Studentdto.class);
    }

    @Override
    public Studentdto createstudent(AddStudentDto addStudentDto) throws DuplicateResouseException {

        Student newStudent = modelMapper.map(addStudentDto, Student.class);

        if (studentRepository.existsByEmail(newStudent.getEmail())) {
            throw new DuplicateResouseException(
                    "Student with this email already exists");
        }

        Student savedStudent = studentRepository.save(newStudent);

        return modelMapper.map(savedStudent, Studentdto.class);
    }
}