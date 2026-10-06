package de.hsrm.mi.web.projekt.anzeige.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.anzeige.AnzeigeRepository;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.BenutzerRepository;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtEvent;

@Service
@Transactional
public class AnzeigeServiceImpl implements AnzeigeService {

    private final AnzeigeRepository anzeigeRepository;
    private final BenutzerRepository benutzerRepository;
    private final ApplicationEventPublisher publisher;

    private final Logger logger =
            LoggerFactory.getLogger(AnzeigeServiceImpl.class);

    public AnzeigeServiceImpl(
            AnzeigeRepository anzeigeRepository,
            BenutzerRepository benutzerRepository,
            ApplicationEventPublisher publisher) {

        this.anzeigeRepository = anzeigeRepository;
        this.benutzerRepository = benutzerRepository;
        this.publisher = publisher;
    }

    @Override
    public Collection<Anzeige> findAllAnzeigen() {

        logger.info("findAllAnzeigen");

        return anzeigeRepository.findAllByOrderByAblaufdatumAsc();
    }

    @Override
    public Optional<Anzeige> findAnzeigeById(long id) {

        logger.info("findAnzeigeById {}", id);

        return anzeigeRepository.findById(id);
    }

    @Override
    public Anzeige saveAnzeige(Anzeige anzeige) {

        logger.info("saveAnzeige {}", anzeige.getTitel());

        boolean istNeu = !anzeigeRepository.existsById(anzeige.getId());

        Anzeige gespeichert = anzeigeRepository.save(anzeige);

        publisher.publishEvent(new FrontendNachrichtEvent(
                FrontendNachrichtEvent.EventTyp.ANZEIGE,
                gespeichert.getId(),
                istNeu
                        ? FrontendNachrichtEvent.Operation.CREATE
                        : FrontendNachrichtEvent.Operation.UPDATE));
         
        // Thread.sleep-Block (1sek)               
        /*               
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        */
        return gespeichert;
        
        }
        

    @Override
    public void deleteAnzeige(long id) {

        logger.info("deleteAnzeige {}", id);

        anzeigeRepository.deleteById(id);

        publisher.publishEvent(new FrontendNachrichtEvent(
                FrontendNachrichtEvent.EventTyp.ANZEIGE,
                id,
                FrontendNachrichtEvent.Operation.DELETE));
    }

    @Override
    public void bestellen(Anzeige a, Benutzer b) {

        logger.info("{} bestellt {}", b.getLoginName(), a.getTitel());

        if (a.getAnbieter() != null
                && a.getAnbieter().equals(b)) {

            throw new AnzeigeException(
                    "Anbieter darf nicht selbst bestellen");
        }

        if (a.getBesteller().contains(b)) {

            throw new AnzeigeException(
                    "Benutzer hat bereits bestellt");
        }

        if (a.getBesteller().size() >= a.getAnzahl()) {

            throw new AnzeigeException(
                    "Anzeige ausverkauft");
        }

        a.getBesteller().add(b);

        anzeigeRepository.save(a);

        publisher.publishEvent(new FrontendNachrichtEvent(
                FrontendNachrichtEvent.EventTyp.ANZEIGE,
                a.getId(),
                FrontendNachrichtEvent.Operation.UPDATE));
    }

    @Override
    public void stornieren(Anzeige a, Benutzer b) {

        logger.info("{} storniert {}", b.getLoginName(), a.getTitel());

        if (!a.getBesteller().contains(b)) {

            throw new AnzeigeException(
                    "Benutzer hat diesen Artikel nicht bestellt");
        }

        a.getBesteller().remove(b);

        anzeigeRepository.save(a);

        publisher.publishEvent(new FrontendNachrichtEvent(
                FrontendNachrichtEvent.EventTyp.ANZEIGE,
                a.getId(),
                FrontendNachrichtEvent.Operation.UPDATE));
    }

    @Override
    public void verlosen() {

        logger.info("Verlosung gestartet");

        Random random = new Random();

        List<Benutzer> benutzer =
                benutzerRepository.findAll();

        List<Anzeige> anzeigen =
                anzeigeRepository.findAll();

       if (benutzer.isEmpty() || anzeigen.isEmpty()) {
            logger.warn("Verlosung abgebrochen: keine Daten vorhanden");
            return;
        }

        // Anbieter zufällig setzen
        for (Anzeige a : anzeigen) {

            Benutzer zufall =
                    benutzer.get(random.nextInt(benutzer.size()));

            a.setAnbieter(zufall);

            anzeigeRepository.save(a);

            logger.info(
                    "Anbieter {} für Anzeige {} gesetzt",
                    zufall.getLoginName(),
                    a.getTitel()
            );
        }

        // Bestellungen zufällig setzen
        for (Benutzer b : benutzer) {

            for (int i = 0; i < 3; i++) {

                Anzeige zufallsAnzeige =
                        anzeigen.get(random.nextInt(anzeigen.size()));

                try {

                    bestellen(zufallsAnzeige, b);

                    logger.info(
                            "{} bekam Anzeige {}",
                            b.getLoginName(),
                            zufallsAnzeige.getTitel()
                    );

                } catch (AnzeigeException e) {

                    logger.warn(
                            "Bestellung fehlgeschlagen für Benutzer {} bei Anzeige {}: {}",
                            b.getLoginName(),
                            zufallsAnzeige.getTitel(),
                            e.getMessage()
                    );
                }
            }
        }

        logger.info("Verlosung beendet");
    }
}