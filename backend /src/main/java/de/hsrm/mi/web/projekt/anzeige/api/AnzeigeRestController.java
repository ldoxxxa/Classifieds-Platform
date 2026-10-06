package de.hsrm.mi.web.projekt.anzeige.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.hsrm.mi.web.projekt.anzeige.mapper.AnzeigeMapper;
import de.hsrm.mi.web.projekt.anzeige.services.AnzeigeException;
import de.hsrm.mi.web.projekt.anzeige.services.AnzeigeService;
import de.hsrm.mi.web.projekt.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

@RestController
@RequestMapping("/api/anzeige")
public class AnzeigeRestController {

    @Autowired
    private AnzeigeService anzeigeService;

    @Autowired
    private AnzeigeMapper anzeigeMapper;

    @Autowired
    private BenutzerService benutzerService;

    @GetMapping("/{id}")
    public AnzeigeDTO getAnzeigeById(@PathVariable long id) {
        return anzeigeService.findAnzeigeById(id)
            .map(anzeigeMapper::anzeigeToDTO)
            .orElseThrow(() -> new AnzeigeNotFoundException(id));
    }

    @GetMapping
    public List<AnzeigeDTO> getAllAnzeigen() {
        return anzeigeService.findAllAnzeigen().stream()
            .sorted((a, b) -> a.getAblaufdatum().compareTo(b.getAblaufdatum()))
            .map(anzeigeMapper::anzeigeToDTO)
            .toList();
    }

    @PostMapping("/{id}/bestellung/{loginName}")
    public void bestellen(@PathVariable long id, @PathVariable String loginName) {
        Anzeige anzeige = anzeigeService.findAnzeigeById(id)
            .orElseThrow(() -> new AnzeigeNotFoundException(id));

        Benutzer benutzer = benutzerService.findBenutzerById(loginName)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Benutzer nicht gefunden"));

        try {
            anzeigeService.bestellen(anzeige, benutzer);
        } catch (AnzeigeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/bestellung/{loginName}")
    public void stornieren(@PathVariable long id, @PathVariable String loginName) {
        Anzeige anzeige = anzeigeService.findAnzeigeById(id)
            .orElseThrow(() -> new AnzeigeNotFoundException(id));

        Benutzer benutzer = benutzerService.findBenutzerById(loginName)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Benutzer nicht gefunden"));

        try {
            anzeigeService.stornieren(anzeige, benutzer);
        } catch (AnzeigeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}