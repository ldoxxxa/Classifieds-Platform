package de.hsrm.mi.web.geo;

import java.util.List;

public interface GeoService {
    List<GeoAdresse> findeAdressen(String such);
}