package pl.grabowski.usapp.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.grabowski.usapp.model.Student;


@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    Page<Student> getStudentsByFirstNameOrLastName(Pageable pageable, String firstName, String lastName);
}
