package com.example.demo.provider;

import com.example.demo.model.WeatherResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class OpenWeatherClient {
    private final String API_KEY = System.getenv("OPENWEATHER_API_KEY");
    private final RestTemplate restTemplate = new RestTemplate();

    public WeatherResponse fetch(String city) {
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s,AU&appid=%s&units=metric", city, API_KEY);
        Map<?, ?> response = restTemplate.getForObject(url, Map.class);
        Map<?, ?> main = (Map<?, ?>) response.get("main");
        Map<?, ?> wind = (Map<?, ?>) response.get("wind");
        return new WeatherResponse(
                ((Number) main.get("temp")).doubleValue(),
                ((Number) wind.get("speed")).doubleValue()
        );
    }
}
