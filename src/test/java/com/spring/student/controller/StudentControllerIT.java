package com.spring.student.controller;

import jdk.net.SocketFlow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.student.Repo.StudentRepo;
import com.spring.student.StudentApplicationIT;
import com.spring.student.model.Student;
import com.spring.student.model.StudentDto;
import com.spring.student.util.DataUtil;
import com.spring.student.util.RequestUtil;

public class StudentControllerIT extends StudentApplicationIT {

    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private DataUtil dataUtil;

    @Test
    public void createStudent_thenValidate() throws JsonProcessingException {
        // Given Student
        Student student = new Student("eman",22,"01113903660",true);

        // When Create Student
        ResponseEntity<String> responseEntity =
                requestUtil.post("/api/create",student,null,String.class);

        // Then Validate
        Assertions.assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        StudentDto studentDto = objectMapper.readValue(responseEntity.getBody(),StudentDto.class);
        Assertions.assertEquals("eman",studentDto.getName());
        Assertions.assertEquals(22,studentDto.getAge());
        Assertions.assertEquals("01113903660",studentDto.getPhone());
        Assertions.assertTrue(studentDto.isActive());

        // Clean Data
        dataUtil.delete(studentDto);
    }

    @Test
    public void getAllStudent_thenValidate() throws JsonProcessingException {
        //Given 2 Student
        createStudent("eman",22,"01113903660",true);
        createStudent("test",22,"010258875",false);

        // When Gat All Student
        ResponseEntity<String> responseEntity =
                requestUtil.get("/api/getAll",null,null,String.class);

        // Then Validate
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        StudentDto[] studentListDto = objectMapper.readValue(responseEntity.getBody(),StudentDto[].class);
        Assertions.assertEquals(2,studentListDto.length);
        Assertions.assertEquals("test",studentListDto[1].getName());
        Assertions.assertEquals(22,studentListDto[1].getAge());
        Assertions.assertEquals("010258875",studentListDto[1].getPhone());
        Assertions.assertFalse(studentListDto[1].isActive());

        // Clean Data
        dataUtil.delete(studentListDto);
    }

    @Test
    public void deleteStudent_thenValidate(){
        // Given Student
        Student student = createStudent("eman",22,"01113903660",true);

        // When Delete Student
        ResponseEntity<String> responseEntity =
                requestUtil.delete(String.format("/api/delete/%s",student.getId()),null,null,String.class);
        // Then Validate
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void editStudent_thenValidate() throws JsonProcessingException {
        // Given Student
        Student student = createStudent("alaa",21,"0122588885",false);
        student.setName("test");

        // When Edit Student
        ResponseEntity<String> responseEntity =
                requestUtil.put("/api/edit",student,null,String.class);

        // Then Validate
        StudentDto studentDto = objectMapper.readValue(responseEntity.getBody(),StudentDto.class);
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        Assertions.assertEquals("test",studentDto.getName());

        // Clean Data
        dataUtil.delete(studentDto);

    }

    @Test
    public void getStudentById_thenValidate() throws JsonProcessingException {
        // Given Student
        Student student = createStudent("alaa",21,"0122588885",false);

        // When Get Student By ID
        ResponseEntity<String> responseEntity =
                requestUtil.get(String.format("/api/get/%s",student.getId()),null,null,String.class);
        StudentDto studentDto = objectMapper.readValue(responseEntity.getBody(),StudentDto.class);

        // Then Validate
        Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        Assertions.assertEquals("alaa",studentDto.getName());
        Assertions.assertEquals(21,studentDto.getAge());
        Assertions.assertEquals("0122588885",studentDto.getPhone());
        Assertions.assertFalse(studentDto.isActive());

        // Clean Data
        dataUtil.delete(studentDto);
    }

    private Student createStudent(String name, int age, String phone, boolean active){
        Student student = new Student(name,age,phone,active);
        return studentRepo.save(student);
    }
}
