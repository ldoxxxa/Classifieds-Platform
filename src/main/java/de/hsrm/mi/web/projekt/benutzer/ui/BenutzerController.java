package de.hsrm.mi.web.projekt.benutzer.ui;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin/benutzer")
@SessionAttributes("formularMap")
public class BenutzerController {

    private static final Logger logger = LoggerFactory.getLogger(BenutzerController.class);

    @ModelAttribute("formularMap")
    public Map<String, BenutzerFormular> initFormularMap() {
        return new HashMap<>();
    }

    @GetMapping("/{loginname}")
    public String benutzerBearbeiten(
            @PathVariable String loginname,
            @ModelAttribute("formularMap") Map<String, BenutzerFormular> formularMap,
            Model model) {

        BenutzerFormular formular = formularMap.get(loginname);

        if (formular == null) {
            formular = new BenutzerFormular();
            formularMap.put(loginname, formular);
            logger.info("Neues Formular für {} angelegt", loginname);
        } else {
            logger.info("Vorhandenes Formular für {} geladen", loginname);
        }

        model.addAttribute("loginname", loginname);
        model.addAttribute("formular", formular);

        return "benutzer/bearbeiten";
    }

    @PostMapping("/{loginname}")
    public String postBenutzerBearbeiten(
            @PathVariable String loginname,
            @Valid @ModelAttribute("formular") BenutzerFormular formular,
            BindingResult bindingResult,
            @ModelAttribute("formularMap") Map<String, BenutzerFormular> formularMap,
            Model model) {

        model.addAttribute("loginname", loginname);

        if (!formular.getPasswort().equals(formular.getPasswortWdh())) {
            bindingResult.rejectValue(
                    "passwortWdh",
                    "benutzer.fehler.passwortwiederholung",
                    "Passworteingaben stimmen nicht überein"
            );
        }

        if (bindingResult.hasErrors()) {
            logger.info("Validierungsfehler bei Formular für {}", loginname);
            model.addAttribute("formular", formular);
            return "benutzer/bearbeiten";
        }

        formularMap.put(loginname, formular);

        logger.info("Formular für {} gespeichert: {}", loginname, formular);

        return "redirect:/admin/benutzer/" + loginname;
    }
}