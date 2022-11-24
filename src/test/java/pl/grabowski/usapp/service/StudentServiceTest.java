package pl.grabowski.usapp.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import pl.grabowski.usapp.Repo.StudentRepository;
import pl.grabowski.usapp.model.Student;
import pl.grabowski.usapp.model.Teacher;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class StudentServiceTest {

    private final TeacherService teacherService = Mockito.mock(TeacherService.class);
    private final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
    StudentService studentService = new StudentService(studentRepository,teacherService);

    @Test
    void studentContainsTeacher_should_return_true_when_student_contains_teacher_id(){
        //given
        Teacher teacher = new Teacher(5L, "Pawel","Grabowski",33, "pawel@gmail.com","Physics",null);
        Student student = new Student("Pawel","Grabowski",33, "pawel@gmail.com","Physics");
        student.addTeacher(teacher);

        //when
        var result = studentService.studentContainsTeacher(student,5L);
        assertThat(result).isTrue();
    }

    @Test
    void studentContainsTeacher_should_return_false_when_student_not_contains_teacher_id(){
        //given
        Teacher teacher = new Teacher(5L, "Pawel","Grabowski",33, "pawel@gmail.com","Physics",null);
        Student student = new Student("Pawel","Grabowski",33, "pawel@gmail.com","Physics");
        student.addTeacher(teacher);

        //when
        var result = studentService.studentContainsTeacher(student,1L);
        assertThat(result).isFalse();
    }

    @Test
    void should_return_empty_list_when_no_students(){
        //given
        List<Student> students = new ArrayList<>();

        Pageable pageable= PageRequest.of(0,5);
        Page<Student> empty = new PageImpl<>(students,pageable,1);
        given(studentRepository.findAll(any(Pageable.class)))
                .willReturn((empty));
        //when
        var result = studentService.getAllStudents(0,"firstName");

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void should_return_student_list_when_students_exist(){
        //given
        Student expectedFirst = new Student(1L, "Pawel","Grabowski",33, "pawel@gmail.com","Physics", List.of());
        Student expectedSecond = new Student(2L, "Jan","Nowak",33, "jan@gmail.com","Physics", List.of());
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Pawel","Grabowski",33, "pawel@gmail.com","Physics", List.of()));
        students.add(new Student(2L, "Jan","Nowak",33, "jan@gmail.com","Physics", List.of()));

        Pageable pageable= PageRequest.of(0,5);
        Page<Student> addedStudents = new PageImpl<>(students,pageable,1);
        given(studentRepository.findAll(any(Pageable.class)))
                .willReturn(addedStudents);
        //when
        var result = studentService.getAllStudents(0,"firstName");

        //then
        assertThat(result).isEqualTo(List.of(expectedFirst,expectedSecond));
    }

    @Test
    void should_return_student_by_id(){
        //given
        Student student = new Student(2L, "Pawel","Grabowski",33, "pawel@gmail.com","Physics", List.of());
        given(studentRepository.findById(any(Long.class))).willReturn(Optional.of(student));

        //when
        Optional<Student> result = studentService.getStudentById(2L);

        //then
        assertThat(result.map(Student::getId)).hasValue(2L);
    }
}