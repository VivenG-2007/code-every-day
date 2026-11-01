package org.example.studentservice;

import org.example.Entity.student;
import org.example.Repository.studentrepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class studentservice {
    private studentrepository studentrepository;

    public studentservice(studentrepository studentrepository) {
        this.studentrepository = studentrepository;
    }
    public student create(student req){
        student res=studentrepository.saveStudent(req);
        return res;
    }
    public student getid(Long id){
        student res=studentrepository.getbyid(id);
        return res;
    }
    public List<student> getall(){
        List<student> res=studentrepository.getall();
        return res;
    }

}
