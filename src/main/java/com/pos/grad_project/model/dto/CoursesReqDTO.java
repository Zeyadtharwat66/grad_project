package com.pos.grad_project.model.dto;

import com.pos.grad_project.model.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;


public record CoursesReqDTO(Grade grade,String category) {

}
