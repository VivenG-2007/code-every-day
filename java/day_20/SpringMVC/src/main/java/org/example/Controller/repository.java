package org.example.Controller;

import org.example.Entity.student;
import org.example.studentservice.studentservice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class repository {
    private studentservice studentservice;
    // Constructor Injection
    public repository(studentservice studentservice) {
        this.studentservice = studentservice;
    }


    @PostMapping("/create")
    public ResponseEntity<student> savestudent(@RequestBody student req){
        student res=studentservice.create(req);
        return  ResponseEntity.ok(res);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<student> getbyid(@PathVariable("id") Long id){
        student res=studentservice.getid(id);
        if(res==null){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(res);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<student>> getall(){
        List<student> res=studentservice.getall();
        if(res.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(res);
    }
}
