package de.hsrm.mi.web.projekt.entities.anzeige;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnzeigeRepository extends JpaRepository<Anzeige, Long> {

    List<Anzeige> findAllByOrderByAblaufdatumAsc();
}