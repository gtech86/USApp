package pl.grabowski.usapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.grabowski.usapp.dto.CreateStudentRequest;
import pl.grabowski.usapp.dto.StudentResponse;
import pl.grabowski.usapp.dto.StudentUpdateRequest;
import pl.grabowski.usapp.model.Student;
import pl.grabowski.usapp.service.StudentService;
import pl.grabowski.usapp.service.TeacherService;
import pl.grabowski.usapp.utils.StructMapper;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/students")
public class StudentController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StructMapper mapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> addNewStudent(@Valid @RequestBody CreateStudentRequest studentToCreate){
        Student studentToAdd = new Student(
                studentToCreate.getFirstName(),
                studentToCreate.getLastName(),
                studentToCreate.getAge(),
                studentToCreate.getMail(),
                studentToCreate.getFieldOfStudy()
        );
        try{
            studentService.addNewStudent(studentToAdd);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        catch (IllegalArgumentException ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudent(
            @RequestParam(required = true) int page,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        List<StudentResponse> studentsResponse = studentService.getAllStudents(page, sortBy)
                .stream()
                .map(student -> new StudentResponse(
                                student.getId(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getAge(),
                                student.getMail(),
                                student.getFieldOfStudy()
                        )
                )
                .collect(Collectors.toList());

        return new ResponseEntity<>(studentsResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> getAllStudentByPersonalData(
            @RequestParam(required = true) int page,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false)  String firstName,
            @RequestParam(required = false)  String lastName
    ){
        List<StudentResponse> studentsResponse = studentService.getAllStudentsByFirstOrLastName(page, sortBy, firstName, lastName)
                .stream()
                .map(student -> new StudentResponse(
                                student.getId(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getAge(),
                                student.getMail(),
                                student.getFieldOfStudy()
                        )
                )
                .collect(Collectors.toList());

        return new ResponseEntity<>(studentsResponse, HttpStatus.OK);
    }

    @PostMapping(path="/{studentId}/teacher/{teacherId}")
    public ResponseEntity<String> assignStudentToCourse(@PathVariable(required = true) Long studentId, @PathVariable(required = true) Long teacherId ){
        var teacher = teacherService.getTeacherById(teacherId);
        var student = studentService.getStudentById(studentId);
        if(student.isPresent() && teacher.isPresent()) {
            studentService.assignTeacher(studentId,teacherId);
            teacherService.assignStudent(teacherId, studentId);
            return new ResponseEntity<>("Teacher added to student", HttpStatus.OK);
        }
        else return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable(required = true) Long id, @RequestBody StudentUpdateRequest studentUpdateData){
        if(studentService.getStudentById(id).isPresent()){
            var studentToUpdate = studentService.getStudentById(id);
            mapper.updateStudentFromDto(studentUpdateData, studentToUpdate.get());
            var updatedStudent = studentService.updateStudent(id, studentToUpdate.get());
            return new ResponseEntity<>(updatedStudent.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<List<StudentResponse>> getStudentsByTeacherId(@PathVariable Long id){
        var teacher = teacherService.getTeacherById(id);
        if(teacher.isPresent()){
            List<StudentResponse> students = studentService.getStudentsByTeacherId(id)
                    .stream()
                    .map(student -> new StudentResponse(
                                    student.getId(),
                                    student.getFirstName(),
                                    student.getLastName(),
                                    student.getAge(),
                                    student.getMail(),
                                    student.getFieldOfStudy()
                            )
                    )
                    .collect(Collectors.toList());
            return ResponseEntity.ok(students);
        }
        return ResponseEntity.notFound().build();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
