package de.hsrm.mi.web.projekt.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GutesPasswortValidator implements ConstraintValidator<GutesPasswort, String> {

    @Override
    public boolean isValid(String passwort, ConstraintValidatorContext context) {

        if (passwort == null || passwort.isBlank()) {
            return true;
        }

        String klein = passwort.toLowerCase();

        return !klein.contains("42")
                && !klein.contains("zweiundvierzig");
    }
}
    

