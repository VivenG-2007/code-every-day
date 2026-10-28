
package in.viven.CRUDSpringBootDemo.service;

import in.viven.CRUDSpringBootDemo.entity.Student;
import in.viven.CRUDSpringBootDemo.repositories.StudentRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepositories studentRepositories;

    public StudentService(StudentRepositories studentRepositories) {
        this.studentRepositories = studentRepositories;
    }

    public Student createStudent(Student studentreq) {
        studentreq.setDeleted(false);
        Student studentres = studentRepositories.save(studentreq);
        return studentres;
    }
    public Student getStudent(Long id){
        Optional<Student> res= studentRepositories.findByIdAndDeletedIsFalse(id);
        return res.orElse(null);
    }
    public List<Student> getAllStudents(){
        List<Student> studentres=studentRepositories.findAllAndDeletedIsNull();
        return studentres;
    }
    public Student updateStudent(Long id,Student studentReq){
        Optional<Student> existing= studentRepositories.findById(id);
        if(existing.isEmpty()){
            return null;
        }
        Student studentToSave=existing.get();
        studentToSave.setAge(studentReq.getAge());
        studentToSave.setName(studentReq.getName());
        studentToSave.setEmail(studentReq.getEmail());
        studentToSave.setRollno(studentReq.getRollno());
        studentToSave.setDeleted(false);
        return studentRepositories.save(studentToSave);
    }
    public Boolean deleteStudent(Long id){
        Boolean res=studentRepositories.existsById(id);
        if(!res){
            return false;
        }
        studentRepositories.deleteById(id);
        return true;
    }

    public Boolean softDeleteStudent(Long id){
        Optional<Student> student = Optional.ofNullable(studentRepositories.deleteByIdAndDeletedIsFalse(id));
        if(student.isEmpty()){
            return false;
        }
        Student studentToSave=student.get();
        studentToSave.setDeleted(true);
        return true;
    }
}
