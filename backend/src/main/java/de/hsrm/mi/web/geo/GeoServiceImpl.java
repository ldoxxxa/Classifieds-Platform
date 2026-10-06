package de.hsrm.mi.web.geo;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GeoServiceImpl implements GeoService {

    private static final Logger log = LoggerFactory.getLogger(GeoServiceImpl.class);

    private static final Set<String> EXCLUDE_TYPES = Set.of(
        "country", "state", "region", "postcode"
    );

    private final NominatimClient nominatimClient;

    public GeoServiceImpl(NominatimClient nominatimClient) {
        this.nominatimClient = nominatimClient;
    }

    @Override
    public List<GeoAdresse> findeAdressen(String such) {
        if (such == null) {
            log.error("findeAdressen() aufgerufen mit null");
            return List.of();
        }

        return nominatimClient.suche(such)
            .stream()
            .filter(a -> !EXCLUDE_TYPES.contains(a.addresstype()))
            .toList();
    }
}