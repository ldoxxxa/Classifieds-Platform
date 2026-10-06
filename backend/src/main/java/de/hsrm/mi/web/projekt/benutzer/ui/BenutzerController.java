package de.hsrm.mi.web.projekt.benutzer.ui;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hsrm.mi.web.projekt.benutzer.mapper.BenutzerMapper;
import de.hsrm.mi.web.projekt.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import de.hsrm.mi.web.geo.GeoService;

//Nimmt HTTP-Anfragen entgegen
@Controller
@RequestMapping("/admin/benutzer")

//Alle URLs in dieser Klasse fangen mit /admin/benutzer an   

public class BenutzerController {

    private static final Logger logger =
            LoggerFactory.getLogger(BenutzerController.class);

    //Dependency Injection -> Spring gibt COntroller automatisch OBjekte die er braucht 
    private final BenutzerService benutzerService;
    private final BenutzerMapper benutzerMapper;
    private final MessageSource messageSource;                          //für mehrsprachige Fehlermeldung 
    private final GeoService geoService;
    private final PasswordEncoder passwordEncoder;

    public BenutzerController(
            BenutzerService benutzerService,
            BenutzerMapper benutzerMapper,
            MessageSource messageSource,
            GeoService geoService,
            PasswordEncoder passwordEncoder
    ) {
        this.benutzerService = benutzerService;
        this.benutzerMapper = benutzerMapper;
        this.messageSource = messageSource;
        this.geoService = geoService;
        this.passwordEncoder = passwordEncoder;

    }

    // /admin/benutzer -> holt alle Benutzer aus DB & gibt sie an liste.html
    @GetMapping
    public String benutzerListe(Model model) {
        model.addAttribute(
                "benutzerliste",
                benutzerService.findAllBenutzer()
        );

        return "benutzer/liste";
    }

    // /admin//benutzer/joghurta -> zeigt Formular an
    @GetMapping("/{loginname}")
    public String benutzerBearbeiten(
        @PathVariable String loginname,         
        Model model,
        HttpSession session
    ) {

        Optional<Benutzer> benutzerOptional =
            benutzerService.findBenutzerById(loginname);

            BenutzerFormular formular;

        if (benutzerOptional.isPresent()) {

            //Benutzer existiert in der DB
            Benutzer benutzer = benutzerOptional.get();
            formular = benutzerMapper.benutzerToBenutzerFormular(benutzer);

            session.setAttribute(

                    "version_" + loginname,
                    benutzer.getVersion()
            );

            // Bestellung alphabetisch sortieren
            var bestellungen = benutzer.getBestellungen()
                .stream()
                .sorted((a1, a2) -> a1.getTitel().compareToIgnoreCase(a2.getTitel()))
                .toList();

            model.addAttribute("bestellungen", bestellungen);

            logger.info("Benutzer {} aus Datenbank geladen", loginname);
        
        } else {

            //Benutzer existiert noch nicht -> leeres Formular 
            formular = new BenutzerFormular();

            model.addAttribute("bestellungen", java.util.List.of());

            logger.info("Neues Formular für {} angelegt", loginname);
        }

        model.addAttribute("loginname", loginname);
        model.addAttribute("formular", formular);

        return "benutzer/bearbeiten";
    }

    //POST Handler empfängt Formulardaten
    @PostMapping("/{loginname}")
    public String postBenutzerBearbeiten(
            @PathVariable String loginname,
            @Valid @ModelAttribute("formular") BenutzerFormular formular,           //Spring prüft alle Validierungsregeln (@Notblank, etc)
            BindingResult bindingResult,                                            //Fängt Validierungsfehler auf (muss nach @Valid stehen)
            Model model,
            HttpSession session
    ) {
        model.addAttribute("loginname", loginname);

        //Passwörter prüfen ob übereinstimmen
        if (formular.getPasswort() != null
                && formular.getPasswortWdh() != null
                && !formular.getPasswort().equals(formular.getPasswortWdh())) {
            bindingResult.rejectValue(
                    "passwortWdh",
                    "benutzer.fehler.passwortwiederholung",
                    "Passworteingaben stimmen nicht überein"
            );
        }

        Optional<Benutzer> vorhandenerBenutzerVorValidierung =
        benutzerService.findBenutzerById(loginname);

        //Neuer Benutzer ohne Passwort prüfen
        if (vorhandenerBenutzerVorValidierung.isEmpty()
                && (formular.getPasswort() == null || formular.getPasswort().isBlank())) {
            bindingResult.rejectValue(
                    "passwort",
                    "benutzer.fehler.passwort.leer",
                    "Neuer Benutzer kann nicht ohne Passwort gespeichert werden."
            );
        }

        // Geo-Check nur bei neuem Benutzer
        if (vorhandenerBenutzerVorValidierung.isEmpty()
                && formular.getAdresse() != null
                && !formular.getAdresse().isBlank()) {

            var treffer = geoService.findeAdressen(formular.getAdresse());

            if (treffer.isEmpty()) {
                bindingResult.rejectValue(
                    "adresse",
                    "adresse.nichtGefunden",
                    "Adresse konnte nicht gefunden werden. Bitte genauer angeben."
                );
            } else {
                formular.setAdresse(treffer.get(0).display_name());
            }
        }

        // Wenn Fehler vorhanden, dann zurück zum Formular 
        if (bindingResult.hasErrors()) {
            logger.info("Validierungsfehler bei Formular für {}", loginname);
            model.addAttribute("formular", formular);
            return "benutzer/bearbeiten";
        }

        // Bei keinem Fehler speichern 
        try {
            Benutzer benutzer =
                    benutzerMapper.benutzerFormularToBenutzer(formular);

            benutzer.setLoginName(loginname);

            Long version =
                    (Long) session.getAttribute("version_" + loginname);

            if (version != null) {
                benutzer.setVersion(version);
            }

            
            Optional<Benutzer> vorhandenerBenutzer =
                    benutzerService.findBenutzerById(loginname);

            // Wenn Passwort leer, dann altes (bereits encodiertes) Passwort aus der DB übernehmen 
            if (benutzer.getPasswort() == null
                    || benutzer.getPasswort().isBlank()) {

                if (vorhandenerBenutzer.isPresent()) {
                    benutzer.setPasswort(
                            vorhandenerBenutzer.get().getPasswort()
                    );

            } else {
            throw new BenutzerException(
                messageSource.getMessage(
                    "benutzer.fehler.passwort.leer",
                    null,
                    LocaleContextHolder.getLocale()
                    )
                );
            }
        } else {
            // Neues Klartext-Passwort wurde eingegeben -> vor dem Speichern encoden
            benutzer.setPasswort(passwordEncoder.encode(benutzer.getPasswort()));
        }

            benutzerService.saveBenutzer(benutzer);

            logger.info("Benutzer {} in Datenbank gespeichert", loginname);

            return "redirect:/admin/benutzer";

        //Exceptions 
        } catch (BenutzerException e) {
            logger.error("Fehler beim Speichern von Benutzer {}", loginname, e);

            model.addAttribute("formular", formular);
            model.addAttribute("info", e.getMessage());

            return "benutzer/bearbeiten";

        } catch (ObjectOptimisticLockingFailureException e) {
            logger.error("Optimistic Locking Fehler bei Benutzer {}", loginname, e);
            model.addAttribute("formular", formular);
            model.addAttribute(
                    "info",
                    "Benutzer wurde zwischenzeitlich geändert."
            );

            return "benutzer/bearbeiten";

        } catch (Exception e) {
            logger.error("Unerwarteter Fehler beim Speichern von Benutzer {}", loginname, e);

            model.addAttribute("formular", formular);
            model.addAttribute("info", e.getMessage());

            return "benutzer/bearbeiten";
        }
    }

    @GetMapping("/{loginname}/delete")
    public String deleteBenutzer(
            @PathVariable String loginname
    ) {
        benutzerService.deleteBenutzerById(loginname);

        logger.info("Benutzer {} gelöscht", loginname);

        return "redirect:/admin/benutzer";
    }
    
    //HTMX GET -> Mini-Formular wenn ich auf E-Mail/Name klicke
    @GetMapping("/{loginname}/hx/feld/{feldname}")
    public String hxFeldAnzeigen(
        @PathVariable String loginname,
        @PathVariable String feldname,
        Model model
    ) {
    Benutzer benutzer = benutzerService.findBenutzerById(loginname)
        .orElseThrow();
    
    String wert = feldname.equals("name") ? benutzer.getName() : benutzer.getEmail();
    
    model.addAttribute("loginName", loginname);
    model.addAttribute("feldname", feldname);
    model.addAttribute("wert", wert);
    
    return "benutzer/eingabefeld :: bearbeiten";

    }
    //HTMX PUT
    @PutMapping("/{loginname}/hx/feld/{feldname}")
    public String hxFeldSpeichern(
        @PathVariable String loginname,
        @PathVariable String feldname,
        @RequestParam("wert") String wert,
        Model model
) {
    model.addAttribute("loginName", loginname);
    model.addAttribute("feldname", feldname);
    
    try {
        Benutzer benutzer = benutzerService.aktualisiereBenutzerAttribut(loginname, feldname, wert);
        String neuerWert = feldname.equals("name") ? benutzer.getName() : benutzer.getEmail();
        model.addAttribute("wert", neuerWert);
        return "benutzer/eingabefeld :: ausgeben";

    } catch (Exception e) {
        Benutzer benutzer = benutzerService.findBenutzerById(loginname).orElseThrow();
        String alterWert = feldname.equals("name") ? benutzer.getName() : benutzer.getEmail();
        model.addAttribute("wert", alterWert);
        model.addAttribute("fehler", true);

    // Bean Validation Nachrichten extrahieren
    String fehlermeldung = "Ungültiger Wert!";
    Throwable ursache = e;
    while (ursache.getCause() != null) {
        ursache = ursache.getCause();
    }
    String msg = ursache.getMessage();
    if (msg != null && msg.contains("interpolatedMessage='")) {
        int start = msg.indexOf("interpolatedMessage='") + "interpolatedMessage='".length();
        int end = msg.indexOf("'", start);
        if (end > start) {
            fehlermeldung = msg.substring(start, end);
        }
    }
    model.addAttribute("fehlermeldung", fehlermeldung);

    return "benutzer/eingabefeld :: bearbeiten";
        }
    }
}