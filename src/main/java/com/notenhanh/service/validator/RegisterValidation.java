package com.notenhanh.service.validator;

import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.*;
public @interface RegisterValidation {
	@Constraint(validatedBy = PasswordValidation.class)
		@Target({ ElementType.TYPE })
		@Retention(RetentionPolicy.RUNTIME)
		@Documented
		public @interface RegisterChecked {

		    String message() default "User register validation failed";

		    Class<?>[] groups() default {};

		    Class<? extends Payload>[] payload() default {};
		}

		
	}


