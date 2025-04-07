package com.example.demo.provider;

import com.example.demo.model.WeatherResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import static com.example.demo.util.Constants.WEATHERSTACK_API_KEY_ENV;
import static com.example.demo.util.Constants.WEATHERSTACK_URL_TEMPLATE;


@Component
public class WeatherStackClient {
    private final String API_KEY = System.getenv(WEATHERSTACK_API_KEY_ENV);
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WeatherStackClient.class);
    
    public WeatherStackClient() {
        this.restTemplate = new RestTemplate();
    }

    // For testing
    public WeatherStackClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse fetch(String city) {
    	
        String url = String.format(WEATHERSTACK_URL_TEMPLATE, API_KEY, city);
        logger.info("Calling WeatherStack API for city '{}'", city);

        try {
            Map<?, ?> response = restTemplate.getForObject(url, Map.class);
            if (response == null || !response.containsKey("current")) {
                throw new IllegalStateException("Incomplete response from WeatherStack");
            }

            Map<?, ?> current = (Map<?, ?>) response.get("current");

            double temperature = ((Number) current.get("temperature")).doubleValue();
            double windSpeed = ((Number) current.get("wind_speed")).doubleValue();

            logger.debug("WeatherStack returned temperature={}°C, windSpeed={} km/h for '{}'",
                    temperature, windSpeed, city);

            return new WeatherResponse(temperature, windSpeed);
        } catch (Exception e) {
            logger.error("Failed to fetch weather from WeatherStack for '{}': {}", city, e.getMessage());
            throw e;
        }
    }
}
