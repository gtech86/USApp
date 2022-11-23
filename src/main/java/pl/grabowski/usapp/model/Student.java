package pl.grabowski.usapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String mail;
    private String fieldOfStudy;

    @ManyToMany
    @JoinTable(name = "students_teachers",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> teachers;

    public Student(String firstName, String lastName, Integer age, String mail, String fieldOfStudy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.mail = mail;
        this.fieldOfStudy = fieldOfStudy;
    }

    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
    }
}