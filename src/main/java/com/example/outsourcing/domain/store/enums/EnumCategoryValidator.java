package com.example.outsourcing.domain.store.enums;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@SuppressWarnings("checkstyle:RegexpMultiline")
public class EnumCategoryValidator implements ConstraintValidator<EnumCategory, String> {

	private EnumCategory annotation;

	@Override
	public void initialize(EnumCategory constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Object[] enumValues = this.annotation.target().getEnumConstants();
		if (enumValues != null) {
			for (Object enumValue : enumValues) {
				if (value.equals(enumValue.toString())) {
					return true;
				}
			}
		}
		return false;
	}

}
