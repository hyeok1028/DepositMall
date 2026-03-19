package com.hana8.hanaro.common.validator;

import com.hana8.hanaro.common.util.AccountNumberFormatter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountNumberValidator implements ConstraintValidator<AccountNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        String normalized = AccountNumberFormatter.normalize(value);
        return normalized != null && normalized.matches("\\d{11}");
    }
}
