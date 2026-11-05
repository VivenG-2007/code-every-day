package com.example.demo.Controller;

import com.example.demo.Dto.Studentdto;
import com.example.demo.Service.implimentations.StudentServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentContoller {
    private final StudentServiceImplementation studentServiceImplementation;

    @GetMapping()
    public List<Studentdto> getStudent(){
        return studentServiceImplementation.getAllStudents();
    }

    @GetMapping("/{id}")
    public Studentdto getStudentById(@PathVariable Long id){
        return studentServiceImplementation.getStudentById(id);
    }
}
