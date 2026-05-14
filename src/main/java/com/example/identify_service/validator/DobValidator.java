package com.example.identify_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {
  private int minYearOld;

  @Override
  public void initialize(DobConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.minYearOld = constraintAnnotation.minYearOld();
  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    long years = ChronoUnit.YEARS.between(value, LocalDate.now());
    if (years >= minYearOld) {
      return true;
    }
    return false;
  }

}
