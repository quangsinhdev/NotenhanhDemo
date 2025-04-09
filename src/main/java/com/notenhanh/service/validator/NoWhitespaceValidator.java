package com.notenhanh.service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Service;
@Service
public class NoWhitespaceValidator implements ConstraintValidator<NoWhitespace, String> {

    @Override
    public void initialize(NoWhitespace constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.contains(" ");
    }
}