package com.example.validation_demo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = DomainNotEmptyValidator.class)
@Target({FIELD, METHOD, PARAMETER})
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
