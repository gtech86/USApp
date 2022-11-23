package pl.grabowski.usapp.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.grabowski.usapp.model.Teacher;

public interface TeacherRepository extends PagingAndSortingRepository<Teacher, Long> {

    Page<Teacher> getTeachersByFirstNameOrLastName(Pageable pageable, String firstName, String lastName);
}
