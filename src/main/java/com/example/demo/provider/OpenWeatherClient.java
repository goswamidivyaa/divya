package com.example.demo.provider;

import com.example.demo.model.WeatherResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import static com.example.demo.util.Constants.OPENWEATHER_URL_TEMPLATE;
import static com.example.demo.util.Constants.OPENWEATHER_API_KEY_ENV;


@Component
public class OpenWeatherClient {
    private final String API_KEY = System.getenv(OPENWEATHER_API_KEY_ENV);
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherClient.class);
    
    public OpenWeatherClient() {
        this.restTemplate = new RestTemplate();
    }
    
    // For testing
    public OpenWeatherClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse fetch(String city) {
        String url = String.format(OPENWEATHER_URL_TEMPLATE, city, API_KEY);
        logger.info("Calling OpenWeatherMap API for city '{}'", city);
        
        try {
            Map<?, ?> response = restTemplate.getForObject(url, Map.class);
            if (response == null || !response.containsKey("main") || !response.containsKey("wind")) {
                throw new IllegalStateException("Incomplete response from OpenWeatherMap");
            }

            Map<?, ?> main = (Map<?, ?>) response.get("main");
            Map<?, ?> wind = (Map<?, ?>) response.get("wind");

            double temp = ((Number) main.get("temp")).doubleValue();
            double windSpeed = ((Number) wind.get("speed")).doubleValue();

            logger.debug("OpenWeatherMap returned temperature={}°C, windSpeed={} km/h for '{}'",
                    temp, windSpeed, city);

            return new WeatherResponse(temp, windSpeed);
            
        } catch (Exception e) {
            logger.error("Failed to fetch weather from OpenWeatherMap for '{}': {}", city, e.getMessage());
            throw e;
        }
    }
}