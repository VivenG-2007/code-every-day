package com.example.demo.Controller;

import com.example.demo.Dto.AddStudentDto;
import com.example.demo.Dto.Studentdto;
import com.example.demo.Service.implimentations.StudentServiceImplementation;
import com.example.demo.exception.DuplicateResouseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok().body(studentServiceImplementation.getStudentById(id)).getBody();
    }
    @PostMapping
    public ResponseEntity<Studentdto>createstudent(@RequestBody AddStudentDto addStudentDto) throws DuplicateResouseException {
        return ResponseEntity.ok().body(studentServiceImplementation.createstudent(addStudentDto));
    }
}
