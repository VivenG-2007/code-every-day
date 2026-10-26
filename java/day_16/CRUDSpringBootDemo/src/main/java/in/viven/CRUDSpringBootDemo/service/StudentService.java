package in.viven.CRUDSpringBootDemo.service;


import in.viven.CRUDSpringBootDemo.entity.Student;
import in.viven.CRUDSpringBootDemo.repositories.StudentRepositories;
import org.springframework.stereotype.Component;

@Component
public class StudentService {
    private StudentRepositories studentRepositories;

    public StudentService(StudentRepositories studentRepositories) {
        this.studentRepositories = studentRepositories;
    }

    public Student createStudent(Student studentreq){
        Student studentres=studentRepositories.SaveStudent(studentreq);
        return  studentres;
    }
}
