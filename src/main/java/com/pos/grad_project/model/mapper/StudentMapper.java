package com.pos.grad_project.model.mapper;

import com.pos.grad_project.model.dto.MyCoursesItemRespDTO;
import com.pos.grad_project.model.dto.StudentRespDTO;
import com.pos.grad_project.model.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel="spring")
public interface StudentMapper {
    public StudentRespDTO toRespDTO(StudentEntity studentEntity);
    public MyCoursesItemRespDTO toMyCoursesItemRespDTO(StudentEntity studentEntity);
}
