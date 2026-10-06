package de.hsrm.mi.web.projekt.anzeige.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import de.hsrm.mi.web.projekt.anzeige.api.AnzeigeDTO;
import de.hsrm.mi.web.projekt.anzeige.ui.AnzeigeFormular;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;

@Component
public class AnzeigeMapper {

    public AnzeigeFormular anzeigeToFormular(Anzeige anzeige) {
        AnzeigeFormular formular = new AnzeigeFormular();
        formular.setId(anzeige.getId());
        formular.setVersion(anzeige.getVersion());
        formular.setTitel(anzeige.getTitel());
        formular.setBeschreibung(anzeige.getBeschreibung());
        formular.setPreis(anzeige.getPreis());
        formular.setAnzahl(anzeige.getAnzahl());
        formular.setAblaufdatum(anzeige.getAblaufdatum());
        return formular;
    }

    public void updateAnzeigeFromFormular(AnzeigeFormular formular, Anzeige anzeige) {
        anzeige.setTitel(formular.getTitel());
        anzeige.setBeschreibung(formular.getBeschreibung());
        anzeige.setPreis(formular.getPreis());
        anzeige.setAnzahl(formular.getAnzahl());
        anzeige.setAblaufdatum(formular.getAblaufdatum());
    }

    public AnzeigeDTO anzeigeToDTO(Anzeige anzeige) {
        return new AnzeigeDTO(
            anzeige.getId(),
            anzeige.getTitel(),
            anzeige.getBeschreibung(),
            anzeige.getPreis(),
            anzeige.getAnzahl(),
            anzeige.getAblaufdatum(),
            anzeige.getAnzahl() - anzeige.getBesteller().size(),
            anzeige.getAnbieter() != null ? anzeige.getAnbieter().getName() : "",
            anzeige.getAnbieter() != null ? anzeige.getAnbieter().getAdresse() : ""
        );
    }

    public List<AnzeigeDTO> anzeigeListToDTO(List<Anzeige> anzeigen) {
        return anzeigen.stream()
            .map(this::anzeigeToDTO)
            .toList();
    }

}  