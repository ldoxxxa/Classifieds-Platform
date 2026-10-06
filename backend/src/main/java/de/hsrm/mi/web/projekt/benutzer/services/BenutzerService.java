package de.hsrm.mi.web.projekt.benutzer.services;

import java.util.Collection;
import java.util.Optional;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

public interface BenutzerService {

    //Benutzer in DB speichern (neu anlegen/updaten)
    Benutzer saveBenutzer(Benutzer benutzer);
    
    //Benutzer anhand loginNamen aus DB holen
    Optional<Benutzer> findBenutzerById(String loginName);

    //Alle Benutzer aus DB holen, aufsteigend nach loginNamen sortiert
    Collection<Benutzer> findAllBenutzer();

    //Benutzer anahnd loginNamen aus DB löschen
    void deleteBenutzerById(String loginName);

    //Einzelnes Attribut eines Benutzer updaten zB nur name/email
    Benutzer aktualisiereBenutzerAttribut(String loginName, String feldname, String wert);
    
}
