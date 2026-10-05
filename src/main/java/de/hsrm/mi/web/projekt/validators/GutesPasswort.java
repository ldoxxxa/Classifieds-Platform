package de.hsrm.mi.web.projekt.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = GutesPasswortValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface GutesPasswort {

    String message() default "{benutzer.fehler.passwort}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}