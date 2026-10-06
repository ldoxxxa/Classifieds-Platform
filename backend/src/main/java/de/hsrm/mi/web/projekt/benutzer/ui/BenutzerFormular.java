package de.hsrm.mi.web.projekt.benutzer.ui;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import de.hsrm.mi.web.projekt.validators.GutesPasswort;

public class BenutzerFormular {

    //@NotBlank = darf nicht null, "" oder " " sein, wenn Formular abgeschickt wird
    @NotBlank
    @Size(min = 3, max = 60, message = "{benutzer.fehler.name.groesse}")
    private String name = "";

    //Leerstring "" als Defaultwert -> wenn Formular zum 1. Mal geladen wird, ist das Feld leer statt null
    @NotBlank
    @Email
    private String email = "";

    private String adresse = "";
    private String rolle = "";
    
    //Prüfen ob 42 oder Zweiundvierzig
    @GutesPasswort
    private String passwort = "";
    
    @GutesPasswort
    private String passwortWdh = "";

    private boolean aktiviert = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getPasswortWdh() {
        return passwortWdh;
    }

    public void setPasswortWdh(String passwortWdh) {
        this.passwortWdh = passwortWdh;
    }

    public boolean isAktiviert() {
        return aktiviert;
    }

    public void setAktiviert(boolean aktiviert) {
        this.aktiviert = aktiviert;
    }

    @Override
    public String toString() {
        return "BenutzerFormular{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", rolle='" + rolle + '\'' +
                ", aktiviert=" + aktiviert +
                '}';
    }
}