package com.example.demo.Service;

import com.example.demo.Dto.AddStudentDto;
import com.example.demo.Dto.Studentdto;
import com.example.demo.exception.DuplicateResouseException;

import java.util.List;

public interface StudentService {
    List<Studentdto> getAllStudents();
    Studentdto getStudentById(Long id);

    Studentdto createstudent(AddStudentDto addStudentDto) throws DuplicateResouseException;
}
