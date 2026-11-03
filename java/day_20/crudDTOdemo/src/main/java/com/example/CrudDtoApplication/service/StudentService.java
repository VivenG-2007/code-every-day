package com.example.CrudDtoApplication.service;
import com.example.CrudDtoApplication.entity.Student;
import com.example.CrudDtoApplication.entity.studentDto;
import com.example.CrudDtoApplication.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public studentDto createStudent(studentDto studentReq) {
        Student student=mapToEntity(studentReq);
        student.setCreatedat(LocalDateTime.now());
        student.setUpdatedat(LocalDateTime.now());
        Student studentres=studentRepository.save(student);
        return mapToDto(studentres);
    }

    public Student getStudent(Long id) {
        Optional<Student> studentResp = studentRepository.findById(id);

        if(studentResp.isPresent()) {
            return studentResp.get();
        }
        return null;
    }

    public List<Student> getAllStudent() {
        List<Student> studentList = studentRepository.findByDeletedIsFalse();
        return studentList;
    }


    public Student updateStudent(Long id, Student studentReq) {
        Optional<Student> existingStudent = studentRepository.findByIdAndDeletedIsFalse(id);

        if(existingStudent.isEmpty()) {
            return null;
        }

        Student studentToSave = existingStudent.get();

        studentToSave.setName(studentReq.getName());
        studentToSave.setRollNo(studentReq.getRollNo());
        studentToSave.setSubject(studentReq.getSubject());
        studentToSave.setEmail(studentReq.getEmail());
        studentToSave.setAge(studentReq.getAge());
        studentToSave.setDeleted(false);

        return studentRepository.save(studentToSave);
    }

    public Boolean deleteStudent(Long id) {
        Boolean isStudent = studentRepository.existsById(id);

        if(!isStudent) return false;

        studentRepository.deleteById(id);

        return true;
    }

    public Boolean deleteStudentSoftly(Long id) {
        Optional<Student> existingStudent =
                studentRepository.findByIdAndDeletedIsFalse(id);

        if(existingStudent.isEmpty()) {
            return false;
        }

        Student studentToSave = existingStudent.get();
        studentToSave.setDeleted(true);
        studentRepository.save(studentToSave);

        return true;
    }

    private Student mapToEntity(studentDto studentDto){
        Student student=new Student();
        student.setId(studentDto.getId());
        student.setAge(studentDto.getAge());
        student.setEmail(studentDto.getEmail());
        student.setName(studentDto.getName());
        student.setRollNo(studentDto.getRollNo());
        student.setSubject(studentDto.getSubject());
        student.setDeleted(false);
        return student;
    }


    private studentDto mapToDto(Student student){
        studentDto studentDto=new studentDto();
        studentDto.setId(student.getId());
        studentDto.setAge(student.getAge());
        studentDto.setEmail(student.getEmail());
        studentDto.setName(student.getName());
        studentDto.setRollNo(student.getRollNo());
        studentDto.setSubject(student.getSubject());
        studentDto.setMessage("Student saved successfully");
        studentDto.setCreatedat(student.getCreatedat());
        studentDto.setUpdatedat(student.getUpdatedat());
        return studentDto;

    }
}