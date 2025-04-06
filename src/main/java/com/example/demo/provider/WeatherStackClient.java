package com.example.demo.provider;

import com.example.demo.model.WeatherResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class WeatherStackClient {
    private final String API_KEY = System.getenv("WEATHERSTACK_API_KEY");
    private final RestTemplate restTemplate = new RestTemplate();

    public WeatherResponse fetch(String city) {
        String url = String.format("http://api.weatherstack.com/current?access_key=%s&query=%s", API_KEY, city);
        Map<?, ?> response = restTemplate.getForObject(url, Map.class);
        Map<?, ?> current = (Map<?, ?>) response.get("current");
        return new WeatherResponse(
                ((Number) current.get("temperature")).doubleValue(),
                ((Number) current.get("wind_speed")).doubleValue()
        );
    }
}
