package de.hsrm.mi.web.projekt.entities.benutzer;

import java.util.HashSet;
import java.util.Set;

import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Benutzer {


    @Id                                                             //Eindeutiger Schlüssel der Tabelle
    @NotBlank
    private String loginName;

    @NotBlank
    @Size(min = 3, max = 60)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String adresse;

    //Darf nicht null sein in der Datenbank
    @NotNull
    private Boolean aktiviert;

    @NotBlank
    private String rolle;

    @NotBlank
    private String passwort;

    //Verhindert überschreiben mit den 2 Browsern
    @Version
    private long version;

    // Ein Benutzer hat viele Anzeigen
    @OneToMany(mappedBy = "anbieter", cascade = CascadeType.REMOVE)     //Wenn Benutzer gelöscht wird, werden seine Anzeigen auch gelöscht 
    private Set<Anzeige> anzeigen = new HashSet<>();

    //Ein Benutzer kann viele Anzeigen bestellen, eine Anzeige kann viele Besteller haben 
    @ManyToMany(mappedBy = "besteller")
    private Set<Anzeige> bestellungen = new HashSet<>();

    //Getter & Setter wie immer bei private 
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

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

    public Boolean getAktiviert() {
        return aktiviert;
    }

    public void setAktiviert(Boolean aktiviert) {
        this.aktiviert = aktiviert;
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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Set<Anzeige> getAnzeigen() {
        return anzeigen;
    }

    public void setAnzeigen(Set<Anzeige> anzeigen) {
        this.anzeigen = anzeigen;
    }

    public Set<Anzeige> getBestellungen() {
        return bestellungen;
    }

    public void setBestellungen(Set<Anzeige> bestellungen) {
        this.bestellungen = bestellungen;
    }

    
    @Override
    public String toString() {
        return "Benutzer{" +                                    //fester Text am Anfang
                "loginName='" + loginName + '\'' +              //loginName = 'joghurta'
                ", name='" + name + '\'' +                      //email = 'joghurta@test.de'
                ", email='" + email + '\'' +                    //passwort weg lassen, man will Passwort in Log-Dateien nicht sehen!
                '}';
    }

    //Zwei Benutzer sind gleich wenn ihr loginName gleich ist
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Benutzer)) {
            return false;
        }

        Benutzer benutzer = (Benutzer) o;

        return loginName != null
                && loginName.equals(benutzer.loginName);
    }

    @Override
    public int hashCode() {

        return loginName != null
                ? loginName.hashCode()
                : 0;
    }
}