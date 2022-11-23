package pl.grabowski.usapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.grabowski.usapp.Repo.TeacherRepository;
import pl.grabowski.usapp.model.Student;
import pl.grabowski.usapp.model.Teacher;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final StudentService studentService;

    public TeacherService(TeacherRepository teacherRepository, @Lazy StudentService studentService) {
        this.teacherRepository = teacherRepository;
        this.studentService = studentService;
    }

    private static final int pageSize = 15;

    public List<Teacher> getAllTeachers(int page, String sortBy) {
        return StreamSupport
                .stream(teacherRepository.findAll(
                        PageRequest.of(page, pageSize, Sort.by(sortBy))).spliterator(), false
                )
                .collect(Collectors.toList());
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    @Transactional
    public void addNewTeacher(Teacher teacher) throws IllegalArgumentException{
        teacherRepository.save(teacher);
    }

    @Transactional
    public Optional<Teacher> assignStudent(Long teacherId, Long studentId) throws IllegalArgumentException{
        var student = studentService.getStudentById(studentId);
        var teacher = getTeacherById(teacherId);
        if(student.isPresent() && teacher.isPresent()){
            teacher.get().addStudent(student.get());
            teacherRepository.save(teacher.get());
            return teacher;
        }
        return Optional.empty();
    }

    public List<Teacher> getAllTeachersByFirstOrLastName(int page, String sortBy, String firstName, String lastName) {
        return StreamSupport
                .stream(teacherRepository.getTeachersByFirstNameOrLastName(
                                PageRequest.of(page, pageSize, Sort.by(sortBy)),
                                firstName,lastName
                        ).spliterator(), false
                )
                .collect(Collectors.toList());
    }

    public List<Teacher> getTeacherByStudentId(Long studentId) {
        return teacherRepository.getTeacherByStudentId(studentId);
    }

    @Transactional
    public Optional<Teacher> updateTeacher(Long id, Teacher teacher){
        var teacherToUpdate = getTeacherById(id);
        if(teacherToUpdate.isEmpty()) return Optional.empty();
        teacherRepository.save(teacher);
        return Optional.of(teacher);

    }
}
