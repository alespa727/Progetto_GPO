package it.edu.maxplanck.gpoProject_Server.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (value == null)
			return false;

		// Minimo 8 caratteri
		return value.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
	}
}
