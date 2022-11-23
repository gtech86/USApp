package pl.grabowski.usapp.utils;


import org.mapstruct.*;
import pl.grabowski.usapp.dto.StudentUpdateRequest;
import pl.grabowski.usapp.dto.TeacherUpdateRequest;
import pl.grabowski.usapp.model.Student;
import pl.grabowski.usapp.model.Teacher;

@Mapper(componentModel = "spring")
public interface StructMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "age", ignore = true)
    void updateStudentFromDto(StudentUpdateRequest dto, @MappingTarget() Student student);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "age", ignore = true)
    void updateTeacherFromDto(TeacherUpdateRequest dto, @MappingTarget Teacher teacher);
}
