package de.hsrm.mi.web.projekt.benutzer.services;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.BenutzerRepository;
import org.springframework.transaction.annotation.Transactional;

//Einzige Klasse die mit BenutzerRespository "sprechen" darf
@Service
public class BenutzerServiceImpl implements BenutzerService {

    private static final Logger logger =
            LoggerFactory.getLogger(BenutzerServiceImpl.class);

    //Dependency Injection -> Spring gibt Respository automatisch rein
    private final BenutzerRepository benutzerRepository;

    public BenutzerServiceImpl(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    @Override
    public Benutzer saveBenutzer(Benutzer benutzer) {
        //.save() = neu anlegen oder updaten
        //Gibt Benutzer zurück mit neuer Version
        logger.info("saveBenutzer {}", benutzer.getLoginName());            //Gibt Benutzer zurück mit neuer Version
        return benutzerRepository.save(benutzer);
    }

    @Override
    public Optional<Benutzer> findBenutzerById(String loginName) {
        logger.info("findBenutzerById {}", loginName);
        return benutzerRepository.findById(loginName);
    }

    @Override
    public Collection<Benutzer> findAllBenutzer() {
        //Alle Benutzer aufsteigend nach loginName sortiert (Datenbank sortiert)
        logger.info("findAllBenutzer");
        return benutzerRepository.findAll(
                Sort.by("loginName").ascending()
        );
    }

    @Override
    @Transactional
    //Wenn etw schiefläuft wird alles rückgängig gemacht
    public void deleteBenutzerById(String loginName) {
        logger.info("deleteBenutzerById {}", loginName);
        Benutzer benutzer = benutzerRepository.findById(loginName)
        .orElseThrow(); //wirft Exception wenn nicht gefunden 
    
    // Benutzer aus allen Anzeigen-Besteller-Listen entfernen
    for (Anzeige anzeige : benutzer.getBestellungen()) {
        anzeige.getBesteller().remove(benutzer);
    }
    benutzer.getBestellungen().clear();
    benutzerRepository.save(benutzer);
    
    //Jetzt erst löschen
    benutzerRepository.deleteById(loginName);
    }


    @Override
    @Transactional
    public Benutzer aktualisiereBenutzerAttribut(String loginName, String feldname, String wert) {
        logger.info("aktualisiereBenutzerAttribut {} {} {}", loginName, feldname, wert);

        //Benutzer aus DB holen
        Benutzer benutzer = benutzerRepository.findById(loginName)
        .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden: " + loginName));
    
        // Welches Feld geändert werden soll
        //Nur name und emai sind erlaubt
        switch (feldname) {
            case "name" -> benutzer.setName(wert);
            case "email" -> benutzer.setEmail(wert);
            default -> throw new RuntimeException("Unbekanntes Feld: " + feldname);
            }
        
        //Geänderten Benutzer speichern & zurückgeben
        return benutzerRepository.save(benutzer);
    }
}