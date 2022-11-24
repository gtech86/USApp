package pl.grabowski.usapp.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.grabowski.usapp.Repo.StudentRepository;
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

    @Transactional
    public boolean removeTeacher(Long studentId, Long teacherId){
        var teacher = teacherService.getTeacherById(teacherId);
        var student = getStudentById(studentId);
        if(student.isPresent() && teacher.isPresent()) {
            studentRepository.removeTeacherFromStudent(studentId, teacherId);
        }
        if(studentContainsTeacher(student.get(), teacherId)){
            return true;
        }
        return false;
    }

    boolean studentContainsTeacher(Student student, Long teacherId){
        return student.getTeachers()
                .stream()
                .anyMatch(teacher -> teacher.getId().equals(teacherId));
    }


    public List<Student> getStudentsByTeacherId(Long teacherId){
        var teacher = teacherService.getTeacherById(teacherId);
        if(teacher.isPresent()){
            return new ArrayList<>(teacher.get().getStudents());
        }
        return new ArrayList<>();
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public Optional<Student> updateStudent(Long id, Student student){
        var studentToUpdate = getStudentById(id);
        if(studentToUpdate.isEmpty()) return Optional.empty();
        studentRepository.save(student);
        return Optional.of(student);

    }
}
