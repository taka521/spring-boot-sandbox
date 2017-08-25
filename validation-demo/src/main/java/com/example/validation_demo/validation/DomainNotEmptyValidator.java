package com.example.validation_demo.validation;

import com.example.validation_demo.domain.Domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DomainNotEmptyValidator implements ConstraintValidator<NotEmpty, Domain<String>> {


    private String message;

    @Override
    public void initialize(NotEmpty constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Domain<String> value, ConstraintValidatorContext context) {
        if (value == null || value.getValue().length() == 0) {
            return false;
        }
        return true;
    }
}
