package pl.grabowski.usapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

import javax.persistence.*;
import java.util.List;

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

    private List<Student> students;

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
}
