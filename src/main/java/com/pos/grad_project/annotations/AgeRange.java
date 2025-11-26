package com.pos.grad_project.annotations;

import com.nimbusds.jose.Payload;
import com.pos.grad_project.annotations.validator.AgeRangeValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeRangeValidator.class)
public @interface AgeRange {
    String message() default "Age must be between {min} and {max} years";
    int min();
    int max();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
