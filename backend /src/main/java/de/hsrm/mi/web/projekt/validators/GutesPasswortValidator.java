package de.hsrm.mi.web.projekt.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GutesPasswortValidator implements ConstraintValidator<GutesPasswort, String> {         //Prüft Annotation @GutesPasswort

    @Override
    public boolean isValid(String passwort, ConstraintValidatorContext context) {

        if (passwort == null || passwort.isBlank()) {   //Leeres Passwort erlaubt -> beim Bearbeiten Benutzers muss man Passwort nicht ändern
            return true;
        }

        String klein = passwort.toLowerCase();         //Alles in Kleinbuchstaben umwandeln (damit zB ZwEiunDvIErzig auch erkannt wird)

        return !klein.contains("42")
                && !klein.contains("zweiundvierzig");
    }
}
    

