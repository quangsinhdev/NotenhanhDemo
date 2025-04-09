package com.notenhanh.service.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Service;

import com.notenhanh.domain.dto.UserAccountDTO.RegisterDTO;
@Service
public class PasswordValidation implements ConstraintValidator<RegisterValidation, RegisterDTO> {

	  @Override
	    public void initialize(RegisterValidation constraintAnnotation) {
	    }

	    @Override
	    public boolean isValid(RegisterDTO registerDTO, ConstraintValidatorContext context) {
	        return registerDTO.getPassword().equals(registerDTO.getConfirmPassword());
	    }
	}