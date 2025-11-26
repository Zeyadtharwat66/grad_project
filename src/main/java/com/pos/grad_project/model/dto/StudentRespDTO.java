package com.pos.grad_project.model.dto;

import com.pos.grad_project.model.enums.Gender;
import com.pos.grad_project.model.enums.Grade;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StudentRespDTO(String username, String profilePictureUrl, String email, String phoneNumber, Gender gender, Grade grade, LocalDate birthDate) {
}
