package de.hsrm.mi.web.projekt.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = GutesPasswortValidator.class)     //Eigentliche Prüflogik ist in GutesPasswortValidator.java-> dort steht ob 42/zweiundvierzig drin ist
@Target({ FIELD })                                          //Darf nur Felder angewendet werden, also nur auf private String passwort etc
@Retention(RUNTIME)                                         //Annotation ist zur Laufzeit verfügbar

public @interface GutesPasswort {

    String message() default "{benutzer.fehler.passwort}";  //Fehlermeldung wenn Validierung fehlschlägt -> {benutzer.fehler.passwort} holt Text aus messages.properties

    Class<?>[] groups() default {};                         //Pflichtfeld die jede Validierungsannotation haben muss

    Class<? extends Payload>[] payload() default {};
}

// GutesPasswort.java ist nur das Etikettdas du man auf ein Feld klebt & sGutesPasswortValidator.java ist die eigentliche Prüfung die dann ausgeführt wird wenn das Formular abgeschickt wird