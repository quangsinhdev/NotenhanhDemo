package com.notenhanh.service.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidatorClass implements ConstraintValidator<EnumValidator, Enum<?>> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValidator annotation) {
        this.enumClass = annotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            // Sử dụng valueOf mà không cần ép kiểu Class<Enum>
            Enum.valueOf((Class<? extends Enum>) enumClass, value.name());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

