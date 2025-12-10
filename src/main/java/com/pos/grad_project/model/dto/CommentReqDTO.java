package com.pos.grad_project.model.dto;

import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

public record CommentReqDTO (String comment, @DefaultValue("0.0") float rate, long lessonId){
}
