package de.hsrm.mi.web.geo;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("https://nominatim.openstreetmap.org")
public interface NominatimClient {

    @GetExchange("/search?format=json&countrycodes=de")
    List<GeoAdresse> suche(@RequestParam("q") String q);
}