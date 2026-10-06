package de.hsrm.mi.web.projekt.benutzer.mapper;

import org.mapstruct.Mapper;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.benutzer.ui.BenutzerFormular;

@Mapper(componentModel = "spring")
public interface BenutzerMapper {

    // Beim Laden aus DB: Entity -> Formular
    // Felder mit gleichem Namen werden automatisch übertragen

    BenutzerFormular benutzerToBenutzerFormular(
            Benutzer benutzer
    );

    // Beim SPEICHERN in DB: Formular -> Entity
    // passwortWdh wird NICHT übertragen (existiert nicht in Entity)
    Benutzer benutzerFormularToBenutzer(
            BenutzerFormular formular
    );
}

