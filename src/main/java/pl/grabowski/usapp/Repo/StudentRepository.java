package pl.grabowski.usapp.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.grabowski.usapp.model.Student;


@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    Page<Student> getStudentsByFirstNameOrLastName(Pageable pageable, String firstName, String lastName);
    @Modifying
    @Query(value = "DELETE FROM STUDENTS_TEACHERS WHERE STUDENT_ID = :studentId AND TEACHER_ID = :teacherId", nativeQuery = true)
    void removeTeacherFromStudent(Long studentId, Long teacherId);
}
