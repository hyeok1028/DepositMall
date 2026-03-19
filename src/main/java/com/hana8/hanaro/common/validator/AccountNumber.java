package com.hana8.hanaro.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = AccountNumberValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface AccountNumber {
    String message() default "account number must be 11 digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
