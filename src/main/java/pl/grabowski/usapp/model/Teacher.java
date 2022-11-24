package pl.grabowski.usapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String mail;
    private String subject;

    @ManyToMany(mappedBy = "teachers")

    private List<Student> students = new ArrayList<>();

    public Teacher(String firstName, String lastName, Integer age, String mail, String subject) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.mail = mail;
        this.subject = subject;
    }

    public void addStudent(Student student){
        this.students.add(student);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) && Objects.equals(firstName, teacher.firstName) && Objects.equals(lastName, teacher.lastName) && Objects.equals(age, teacher.age) && Objects.equals(mail, teacher.mail) && Objects.equals(subject, teacher.subject) && Objects.equals(students, teacher.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, age, mail, subject, students);
    }
}
