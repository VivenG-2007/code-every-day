package com.example.demo.Service;

import com.example.demo.Dto.Studentdto;

import java.util.List;

public interface StudentService {
    List<Studentdto> getAllStudents();
    Studentdto getStudentById(Long id);
}
