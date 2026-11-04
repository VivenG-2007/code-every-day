package com.example.demo.Controller;

import com.example.demo.Dto.Studentdto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentContoller {

    @GetMapping()
    public Studentdto getStudent(){
        return new Studentdto(1L,"viven","vivu");
    }
}
