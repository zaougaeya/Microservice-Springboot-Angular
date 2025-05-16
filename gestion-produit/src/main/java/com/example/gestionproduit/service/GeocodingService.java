package com.example.gestionproduit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
@Service
public class GeocodingService {
    
    private final RestTemplate restTemplate;
    private final String nominatimUrl = "https://nominatim.openstreetmap.org/search";
    
    @Value("${nominatim.user-agent}")
    private String userAgent;
    
    @Value("${nominatim.request.delay:1000}")
    private long requestDelay;
    
    private long lastRequestTime = 0;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getZoneFromAddress(String address) {
        try {
            respectRateLimit();
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", userAgent);
            
            URI url = UriComponentsBuilder.fromHttpUrl(nominatimUrl + "/search")
                .queryParam("q", address)
                .queryParam("format", "json")
                .queryParam("limit", "1")
                .build()
                .toUri();

            RequestEntity<?> request = RequestEntity.get(url)
                .headers(headers)
                .build();

            Map<String, Object>[] results = restTemplate.exchange(request, Map[].class).getBody();

            if (results != null && results.length > 0) {
                return extractZoneFromGeocodeResult(results[0]);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du géocodage: " + e.getMessage());
        }
        return "default_zone";
    }

    private synchronized void respectRateLimit() throws InterruptedException {
        long elapsed = System.currentTimeMillis() - lastRequestTime;
        if (elapsed < requestDelay) {
            Thread.sleep(requestDelay - elapsed);
        }
        lastRequestTime = System.currentTimeMillis();
    }

    private String extractZoneFromGeocodeResult(Map<String, Object> result) {
        // Hierarchie: quartier > banlieue > ville > municipalité > état
        String[] zoneKeys = {"neighbourhood", "suburb", "city", "municipality", "state"};
        
        for (String key : zoneKeys) {
            if (result.containsKey(key)) {
                return ((String) result.get(key)).toLowerCase().replace(" ", "_");
            }
        }
        return "default_zone";
    }
}