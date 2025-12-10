package com.pos.grad_project.model.mapper;

import com.pos.grad_project.model.dto.CoursePageResDTO;
import com.pos.grad_project.model.dto.NoteResDTO;
import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.NotesEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel="spring")
public interface NotesMapper {
    public NoteResDTO toRespDTO(NotesEntity notesEntity);

}
