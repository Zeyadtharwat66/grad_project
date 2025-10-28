package com.pos.grad_project.model.dto;

import com.pos.grad_project.model.enums.Gender;
import com.pos.grad_project.model.enums.Grade;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDate;

public record RegisterDTO(String username, String password, @DefaultValue("STUDENT") String role,
                          LocalDate birthDate, Grade grade,
                          Gender gender,String email,String phoneNumber) {}
