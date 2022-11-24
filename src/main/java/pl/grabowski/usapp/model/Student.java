package pl.grabowski.usapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "teacher_id")}
    )
    private List<Teacher> teachers = new ArrayList<>();


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(age, student.age) && Objects.equals(mail, student.mail) && Objects.equals(fieldOfStudy, student.fieldOfStudy) && Objects.equals(teachers, student.teachers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, age, mail, fieldOfStudy, teachers);
    }
}

