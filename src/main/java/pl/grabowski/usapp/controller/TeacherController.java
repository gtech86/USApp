package pl.grabowski.usapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.grabowski.usapp.dto.*;
import pl.grabowski.usapp.model.Teacher;
import pl.grabowski.usapp.service.StudentService;
import pl.grabowski.usapp.service.TeacherService;
import pl.grabowski.usapp.utils.StructMapper;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/teachers")
public class TeacherController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StructMapper mapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Teacher> addNewTeacher(@Valid @RequestBody CreateTeacherRequest teacherToCreate){
        Teacher teacherToAdd = new Teacher(
                teacherToCreate.getFirstName(),
                teacherToCreate.getLastName(),
                teacherToCreate.getAge(),
                teacherToCreate.getMail(),
                teacherToCreate.getMail()
        );
        try{
            teacherService.addNewTeacher(teacherToAdd);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        catch (IllegalArgumentException ex){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public ResponseEntity<List<TeacherResponse>> getAllTeachers(
            @RequestParam(required = true) int page,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        var teacherResponse = teacherService.getAllTeachers(page, sortBy)
                .stream()
                .map(student -> new TeacherResponse(
                                student.getId(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getAge(),
                                student.getMail(),
                                student.getSubject()
                        )
                )
                .collect(Collectors.toList());

        return new ResponseEntity<>(teacherResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeacherResponse>> getAllTeacherByPersonalData(
            @RequestParam(required = true) int page,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false)  String firstName,
            @RequestParam(required = false)  String lastName
    ){
        List<TeacherResponse> teacherResponse = teacherService.getAllTeachersByFirstOrLastName(page, sortBy, firstName, lastName)
                .stream()
                .map(teacher -> new TeacherResponse(
                        teacher.getId(),
                        teacher.getFirstName(),
                        teacher.getLastName(),
                        teacher.getAge(),
                        teacher.getMail(),
                        teacher.getSubject()
                        )
                )
                .collect(Collectors.toList());

        return new ResponseEntity<>(teacherResponse, HttpStatus.OK);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<List<TeacherResponse>> getTeachersByStudentId(@PathVariable Long id){
        var student = studentService.getStudentById(id);
        if(student.isPresent()){
            List<TeacherResponse> teachers = teacherService.getTeacherByStudentId(id)
                    .stream()
                    .map(teacher -> new TeacherResponse(
                                    teacher.getId(),
                                    teacher.getFirstName(),
                                    teacher.getLastName(),
                                    teacher.getAge(),
                                    teacher.getMail(),
                                    teacher.getSubject()
                            )
                    )
                    .collect(Collectors.toList());
            return ResponseEntity.ok(teachers);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path="/{teacherId}/student/{studentId}")
    public ResponseEntity<String> assignStudentToTeacher(@PathVariable(required = true) Long teacherId, @PathVariable(required = true) Long studentId){
        var teacher = teacherService.getTeacherById(teacherId);
        var student = studentService.getStudentById(studentId);
        if(student.isPresent() && teacher.isPresent()) {
            studentService.assignTeacher(studentId,teacherId);
            teacherService.assignStudent(teacherId, studentId);
            return new ResponseEntity<>("StudentAddedToTeacher", HttpStatus.OK);
        }
        else return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable(required = true) Long id, @RequestBody TeacherUpdateRequest teacherUpdateData){
        if(teacherService.getTeacherById(id).isPresent()){
            var teacherToUpdate = teacherService.getTeacherById(id);
            mapper.updateTeacherFromDto(teacherUpdateData, teacherToUpdate.get());
            var updatedTeacher = teacherService.updateTeacher(id, teacherToUpdate.get());
            return new ResponseEntity<>(updatedTeacher.get(), HttpStatus.OK);
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
