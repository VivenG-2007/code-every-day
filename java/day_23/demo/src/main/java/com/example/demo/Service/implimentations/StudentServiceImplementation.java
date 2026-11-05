package com.example.demo.Service.implimentations;

import com.example.demo.Dto.Studentdto;
import com.example.demo.Entity.Student;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImplementation implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Studentdto> getAllStudents() {
        List<Student> students=studentRepository.findAll();
         return students
                .stream()
                .map(student -> {
                    return modelMapper.map(student,Studentdto.class);
                })
                .toList();
    }

    @Override
    public Studentdto getStudentById(Long id){
        Student student=studentRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Student not found with id"+id));
        return modelMapper.map(student,Studentdto.class);
    }
}
