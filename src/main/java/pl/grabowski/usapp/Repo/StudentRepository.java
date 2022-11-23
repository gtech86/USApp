package pl.grabowski.usapp.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.grabowski.usapp.dto.StudentResponse;
import pl.grabowski.usapp.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    @Query(value = "select students " +
            "from Student students " +
            "join Teacher teachers" +
            " where teachers.id = :teacherId")
    List<Student> getStudentsByTeacherId(Long teacherId);

    Page<Student> getStudentsByFirstNameOrLastName(Pageable pageable, String firstName, String lastName);
}
