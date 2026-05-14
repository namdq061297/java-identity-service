package com.example.identify_service.validator;


import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
  String message() default "Invalid date of birth";

  int min() default 10;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
