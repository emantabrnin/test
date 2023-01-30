package com.spring.student.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.student.Repo.StudentRepo;
import com.spring.student.model.Student;
import com.spring.student.model.StudentDto;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentService studentService;

    @Mock
    private StudentRepo studentRepo;

    @Test
    public void createStudent_thenValidate() throws Exception {
        studentService = new StudentServiceImpl(studentRepo);
        Student student = createStudent("eman",20,"01113903660",true);

        Mockito.when(studentRepo.save(student)).thenReturn(createStudentWithID(1L,"eman",20,"01113903660",true));

        StudentDto studentDto = studentService.createStudent(student);
        Assertions.assertEquals(1,studentDto.getId());
        Assertions.assertEquals("eman",studentDto.getName());
        Assertions.assertEquals(20,studentDto.getAge());
        Assertions.assertEquals("01113903660",studentDto.getPhone());
        Assertions.assertTrue(studentDto.isActive());
    }

    @Test
    public void createStudent_thenThrowException() throws Exception {
        studentService = new StudentServiceImpl(studentRepo);
        Student student = createStudentWithID(1L,"eman",20,"01113903660",true);

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> studentService.createStudent(student));

        Assertions.assertEquals("you must not send id",exception.getMessage());
    }

    @Test
    public void getAllStudent_thenValidate(){
        studentService = new StudentServiceImpl(studentRepo);
        Mockito.when(studentRepo.findAll()).thenReturn(studentList());
        List<StudentDto> studentListDto = studentService.getAllStudent();// 0 1 2
        Assertions.assertEquals(5,studentListDto.size());
        StudentDto studentDto = studentListDto.get(2);
        Assertions.assertEquals(3,studentDto.getId());
        Assertions.assertEquals("student3",studentDto.getName());
        Assertions.assertEquals(23,studentDto.getAge());
        Assertions.assertEquals("phone3",studentDto.getPhone());
        Assertions.assertFalse(studentDto.isActive());
    }

    @Test
    public void editStudent_thenValidate(){
        studentService = new StudentServiceImpl(studentRepo);
        Student student = createStudentWithID(1L,"eman",20,"01113903660",true);

        Mockito.when(studentRepo.save(student)).thenReturn(createStudentWithID(1L,"test",20,"01113903660",true));

        StudentDto studentDto = studentService.editStudent(student);
        Assertions.assertEquals(1,studentDto.getId());
        Assertions.assertEquals("test",studentDto.getName());
        Assertions.assertEquals(20,studentDto.getAge());
        Assertions.assertEquals("01113903660",studentDto.getPhone());
        Assertions.assertTrue(studentDto.isActive());
    }

    @Test
    public void getStudentById_thenValidate(){
        studentService = new StudentServiceImpl(studentRepo);
        Student student = createStudentWithID(1L,"eman",20,"01113903660",true);
        Mockito.when(studentRepo.findById(1L)).thenReturn(Optional.of(student));
        StudentDto studentDto = studentService.getStudentById(1L);
        Assertions.assertEquals(1,studentDto.getId());
        Assertions.assertEquals("eman",studentDto.getName());
        Assertions.assertEquals(20,studentDto.getAge());
        Assertions.assertEquals("01113903660",studentDto.getPhone());
        Assertions.assertTrue(studentDto.isActive());
    }

    @Test
    public void deleteStudent_thenValidate(){
        studentService = new StudentServiceImpl(studentRepo);
        studentService.deleteStudent(1L);
        Mockito.verify(studentRepo,Mockito.times(1)).deleteById(1L);
    }
    private Student createStudent(String name,int age,String phone, boolean active){
        return new Student(name,age,phone,active);
    }
    private Student createStudentWithID(Long id, String name,int age,String phone, boolean active){
        return new Student(id,name,age,phone,active);
    }
    private List<Student> studentList(){
        List<Student> students = new ArrayList<>();
        for (int i=1;i<=5;i++){// 1 2 3 4 5
            students.add(createStudentWithID((long)i,"student"+i,20+i,"phone"+i,false));
        }
        return students;
    }
}
