package pl.grabowski.usapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.grabowski.usapp.Repo.StudentRepository;
import pl.grabowski.usapp.Repo.TeacherRepository;
import pl.grabowski.usapp.dto.StudentResponse;
import pl.grabowski.usapp.model.Student;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherService teacherService;
    private static final int pageSize = 15;

    public StudentService(StudentRepository studentRepository, @Lazy TeacherService teacherService) {
        this.studentRepository = studentRepository;
        this.teacherService = teacherService;
    }

    public List<Student> getAllStudents(int page, String sortBy) {
        return StreamSupport
                .stream(studentRepository.findAll(
                        PageRequest.of(page, pageSize, Sort.by(sortBy))).spliterator(), false
                )
                .collect(Collectors.toList());
    }

    public List<Student> getAllStudentsByFirstOrLastName(int page, String sortBy, String firstName, String lastName) {
        return StreamSupport
                .stream(studentRepository.getStudentsByFirstNameOrLastName(
                        PageRequest.of(page, pageSize, Sort.by(sortBy)),
                        firstName,lastName
                        ).spliterator(), false
                )
                .collect(Collectors.toList());
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Transactional
    public void addNewStudent(Student student) throws IllegalArgumentException{
        studentRepository.save(student);
    }

    @Transactional
    public Optional<Student> assignTeacher(Long studentId, Long teacherId) throws IllegalArgumentException{
        var teacher = teacherService.getTeacherById(teacherId);
        var student = getStudentById(studentId);
        student.get().addTeacher(teacher.get());
        studentRepository.save(student.get());
        return student;
    }

    public List<Student> getStudentsByTeacherId(Long teacherId){
        return studentRepository.getStudentsByTeacherId(teacherId);
    }


    @Transactional
    public Optional<Student> updateStudent(Long id, Student student){
        var studentToUpdate = getStudentById(id);
        if(studentToUpdate.isEmpty()) return Optional.empty();
        studentRepository.save(student);
        return Optional.of(student);

    }
}
