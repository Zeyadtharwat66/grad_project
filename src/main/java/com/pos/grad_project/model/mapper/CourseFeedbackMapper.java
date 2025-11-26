package com.pos.grad_project.model.mapper;

import com.pos.grad_project.model.dto.CourseFeedbackReqDTO;
import com.pos.grad_project.model.dto.CourseFeedbackResDTO;
import com.pos.grad_project.model.dto.CourseFeedbackUpdateReqDTO;
import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.CourseFeedbackEntity;
import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.repository.CourseRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel="spring")
public interface CourseFeedbackMapper {
    public CourseFeedbackResDTO toRespDTO(CourseFeedbackEntity courseFeedbackEntity);
    @Mapping(target = "course", source = "courseId", qualifiedByName = "mapCourse")
    @Mapping(target = "student", source = "studentId", qualifiedByName = "mapStudent")
    CourseFeedbackEntity toEntity(CourseFeedbackReqDTO dto);
    @Mapping(target = "course", source = "courseId", qualifiedByName = "mapCourse")
    @Mapping(target = "student", source = "studentId", qualifiedByName = "mapStudent")
    CourseFeedbackEntity toEntity(CourseFeedbackUpdateReqDTO dto);

    @Named("mapCourse")
    default CourseEntity mapCourse(Long id) {
        if (id == null) return null;
        CourseEntity course = new CourseEntity();
        course.setId(id);
        return course;
    }

    @Named("mapStudent")
    default StudentEntity mapStudent(Long id) {
        if (id == null) return null;
        StudentEntity student = new StudentEntity();
        student.setId(id);
        return student;
    }
}
