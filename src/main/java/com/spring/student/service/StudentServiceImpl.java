package com.spring.student.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.student.Repo.StudentRepo;
import com.spring.student.model.Student;
import com.spring.student.model.StudentDto;

@Service
public class StudentServiceImpl implements StudentService{

    private StudentRepo studentRepo;

    @Autowired
    public StudentServiceImpl(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    @Override
    public StudentDto createStudent(Student student) throws Exception {
        if (student.getId() > 0 || student.getId() < 0) {
            throw new Exception("you must not send id");
        }
        return studentDto(studentRepo.save(student)); // not send id
    }

    @Override
    public List<StudentDto> getAllStudent() {
        return studentListDto(studentRepo.findAll());
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepo.deleteById(id);
    }

    @Override
    public StudentDto editStudent(Student student) {
        return studentDto(studentRepo.save(student)); // send id
    }

    @Override
    public StudentDto getStudentById(Long id) {
        return studentDto(studentRepo.findById(id).get());
    }

    private StudentDto studentDto(Student student){
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setName(student.getName());
        studentDto.setAge(student.getAge());
        studentDto.setPhone(student.getPhone());
        studentDto.setActive(student.isActive());
        return studentDto;
    }
    private List<StudentDto> studentListDto(List<Student> students){
        List<StudentDto> studentListDto = new ArrayList<>();
        for (Student student: students){
            StudentDto studentDto = new StudentDto();
            studentDto.setId(student.getId());
            studentDto.setName(student.getName());
            studentDto.setAge(student.getAge());
            studentDto.setPhone(student.getPhone());
            studentDto.setActive(student.isActive());
            studentListDto.add(studentDto);
        }
        return studentListDto;
    }
}
