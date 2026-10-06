package de.hsrm.mi.web.projekt.anzeige.services;

import java.util.Collection;
import java.util.Optional;

import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

public interface AnzeigeService {

    Collection<Anzeige> findAllAnzeigen();

    Optional<Anzeige> findAnzeigeById(long id);

    Anzeige saveAnzeige(Anzeige anzeige);

    void deleteAnzeige(long id);

    void bestellen(Anzeige a, Benutzer b);

    void stornieren(Anzeige a, Benutzer b);

    void verlosen();
}