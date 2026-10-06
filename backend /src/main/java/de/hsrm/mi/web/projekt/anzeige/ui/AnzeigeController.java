package de.hsrm.mi.web.projekt.anzeige.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import de.hsrm.mi.web.projekt.anzeige.mapper.AnzeigeMapper;
import de.hsrm.mi.web.projekt.anzeige.services.AnzeigeException;
import de.hsrm.mi.web.projekt.anzeige.services.AnzeigeService;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import jakarta.validation.Valid;

@Controller
public class AnzeigeController {

    private final AnzeigeService anzeigeService;
    private final AnzeigeMapper anzeigeMapper;

    private final Logger logger =
            LoggerFactory.getLogger(AnzeigeController.class);

    public AnzeigeController(
            AnzeigeService anzeigeService,
            AnzeigeMapper anzeigeMapper) {

        this.anzeigeService = anzeigeService;
        this.anzeigeMapper = anzeigeMapper;
    }

    @GetMapping("/admin/anzeige")
    public String zeigeAnzeigenListe(Model model) {

        logger.info("GET /admin/anzeige");

        model.addAttribute("anzeigen", anzeigeService.findAllAnzeigen());

        return "anzeige/liste";
    }

    @GetMapping("/admin/anzeige/verlosung")
    public String starteVerlosung() {

        logger.info("GET /admin/anzeige/verlosung");

        try {
            anzeigeService.verlosen();
        } catch (AnzeigeException e) {
            logger.warn("Verlosung nicht möglich: {}", e.getMessage());
        }

        return "redirect:/admin/anzeige";
    }

    @GetMapping("/admin/anzeige/{id}")
    public String zeigeAnzeigeFormular(
            @PathVariable long id,
            Model model) {

        logger.info("GET /admin/anzeige/{}", id);

        AnzeigeFormular formular;

        if (id == 0) {
            formular = new AnzeigeFormular();
        } else {
            Anzeige anzeige = anzeigeService.findAnzeigeById(id)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Anzeige nicht gefunden: " + id));

            formular = anzeigeMapper.anzeigeToFormular(anzeige);
        }

        model.addAttribute("anzeigeFormular", formular);

        return "anzeige/bearbeiten";
    }

    @PostMapping("/admin/anzeige/{id}")
    public String speichereAnzeige(
            @PathVariable long id,
            @Valid AnzeigeFormular anzeigeFormular,
            BindingResult bindingResult,
            Model model) {

        logger.info("POST /admin/anzeige/{}", id);

        if (bindingResult.hasErrors()) {
            return "anzeige/bearbeiten";
        }

        Anzeige anzeige;

        if (id == 0) {
            anzeige = new Anzeige();
        } else {
            anzeige = anzeigeService.findAnzeigeById(id)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Anzeige nicht gefunden: " + id));
        }

        anzeigeMapper.updateAnzeigeFromFormular(
                anzeigeFormular,
                anzeige
        );

        anzeigeService.saveAnzeige(anzeige);

        return "redirect:/admin/anzeige";
    }

    @GetMapping("/admin/anzeige/{id}/delete")
    public String loescheAnzeige(@PathVariable long id) {

        logger.info("GET /admin/anzeige/{}/delete", id);

        anzeigeService.deleteAnzeige(id);

        return "redirect:/admin/anzeige";
    }
}