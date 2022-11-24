package pl.grabowski.usapp.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.grabowski.usapp.model.Teacher;

public interface TeacherRepository extends PagingAndSortingRepository<Teacher, Long> {

    Page<Teacher> getTeachersByFirstNameOrLastName(Pageable pageable, String firstName, String lastName);
    @Modifying
    @Query(value = "DELETE FROM STUDENTS_TEACHERS WHERE TEACHER_ID = :teacherId AND STUDENT_ID = :studentId", nativeQuery = true)
    void removeStudentFromTeacher(Long teacherId, Long studentId);
}
