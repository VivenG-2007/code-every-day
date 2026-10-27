package in.viven.CRUDSpringBootDemo.controller;

import in.viven.CRUDSpringBootDemo.entity.Student;
import in.viven.CRUDSpringBootDemo.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //create
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student){
        Student createdStudent=studentService.createStudent(student);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdStudent);

    }

    //read
    @GetMapping("/get/{id}")
    public ResponseEntity<Student>getStudent(@PathVariable Long id){
        Student studentRes=studentService.getStudent(id);
        if(studentRes==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(studentRes);
    }
    @GetMapping("/get/all")
    public ResponseEntity<List<Student>> getstudent(){
        List<Student> studentres=studentService.getAllStudents();
        if(studentres.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentres);
    }

    //update
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable Long id,@RequestBody Student student){
        Student studentres=studentService.updateStudent(id,student);
        if(studentres==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentres);
    }
    //delete

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id){
        Boolean studentres=studentService.deleteStudent(id);
        if(!studentres){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("record deleted");
    }
}
