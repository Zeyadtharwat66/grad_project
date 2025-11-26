package com.pos.grad_project.model.dto;

import com.pos.grad_project.annotations.AgeRange;
import com.pos.grad_project.model.enums.Gender;
import com.pos.grad_project.model.enums.Grade;
import jakarta.validation.constraints.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDate;

public record RegisterDTO(@NotBlank(message = "username is required")
                          @Size(min=3,max = 20,message = "username must be min 3 and max 20")
                          @Pattern(
                                  regexp = "^[A-Za-z][A-Za-z0-9]*$",
                                  message = "Username must start with a letter and contain only letters and numbers"
                          )
                          String username,
                          @NotBlank(message = "password is required")
                          @Pattern(regexp ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "Write Strong Password")
                          String password,
                          @DefaultValue("STUDENT")
                          String role,
                          @AgeRange(min = 12, max = 18, message = "Age must be between 12 and 18 years")
                          LocalDate birthDate,
                          @NotNull(message = "grade is required")
                          Grade grade,
                          @NotNull(message = "gender is required")
                          Gender gender,
                          @NotBlank(message = "email is required")
                          @Email(message = "Invalid email format")
                          String email,
                          @NotBlank(message = "phoneNumber is required")
                          @Pattern(regexp = "^(01[0-2,5]{1}[0-9]{8})$", message = "Invalid Egyptian phone number")
                          String phoneNumber) {}
