package pl.grabowski.usapp.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.grabowski.usapp.model.Student;
import pl.grabowski.usapp.model.Teacher;

import java.util.List;

public interface TeacherRepository extends PagingAndSortingRepository<Teacher, Long> {

    @Query(value = "select teachers " +
            "from Teacher teachers " +
            "join Student student" +
            " where student.id = :studentId")
    List<Teacher> getTeacherByStudentId(Long studentId);

    Page<Teacher> getTeachersByFirstNameOrLastName(Pageable pageable, String firstName, String lastName);
}
