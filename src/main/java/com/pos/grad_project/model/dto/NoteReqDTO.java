package com.pos.grad_project.model.dto;

import java.time.Duration;
public record NoteReqDTO(String text, Duration time,long lessonId) {

}
