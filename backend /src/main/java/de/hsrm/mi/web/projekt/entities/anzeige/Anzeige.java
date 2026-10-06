package de.hsrm.mi.web.projekt.entities.anzeige;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
public class Anzeige {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Version
    private long version;

    @NotBlank
    @Size(min = 3, max = 40)
    private String titel;

    @NotNull
    @Size(min = 17)
    private String beschreibung;

    @PositiveOrZero
    private int preis;

    @PositiveOrZero
    private int anzahl;

    @NotNull
    @Future
    private LocalDate ablaufdatum;

    @ManyToOne
    private Benutzer anbieter;

    @ManyToMany
    private Set<Benutzer> besteller = new HashSet<>();

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getPreis() {
        return preis;
    }

    public void setPreis(int preis) {
        this.preis = preis;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public LocalDate getAblaufdatum() {
        return ablaufdatum;
    }

    public void setAblaufdatum(LocalDate ablaufdatum) {
        this.ablaufdatum = ablaufdatum;
    }

    public Benutzer getAnbieter() {
        return anbieter;
    }

    public void setAnbieter(Benutzer anbieter) {
        this.anbieter = anbieter;
    }

    public Set<Benutzer> getBesteller() {
        return besteller;
    }

    public void setBesteller(Set<Benutzer> besteller) {
        this.besteller = besteller;
    }
}