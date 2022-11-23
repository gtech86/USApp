package pl.grabowski.usapp.utils;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.grabowski.usapp.dto.StudentUpdateRequest;
import pl.grabowski.usapp.dto.TeacherUpdateRequest;
import pl.grabowski.usapp.model.Student;
import pl.grabowski.usapp.model.Teacher;

@Mapper(componentModel = "spring")
public interface StructMapper {
@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudentFromDto(StudentUpdateRequest dto, @MappingTarget Student student);
    void updateTeacherFromDto(TeacherUpdateRequest dto, @MappingTarget Teacher teacher);
}
