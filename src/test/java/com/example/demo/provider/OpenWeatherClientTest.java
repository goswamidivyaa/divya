package com.example.demo.provider;

import com.example.demo.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenWeatherClientTest {

    private RestTemplate restTemplate;
    private OpenWeatherClient client;

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        client = new OpenWeatherClient(restTemplate);
    }

    @Test
    void shouldReturnValidWeatherResponse() {
        String city = "melbourne";

        Map<String, Object> main = new HashMap<>();
        main.put("temp", 23.4);

        Map<String, Object> wind = new HashMap<>();
        wind.put("speed", 11.1);

        Map<String, Object> response = new HashMap<>();
        response.put("main", main);
        response.put("wind", wind);

		/*
		 * String mockUrl = String.format(
		 * "http://api.openweathermap.org/data/2.5/weather?q=%s,AU&appid=%s&units=metric",
		 * city, System.getenv("OPENWEATHER_API_KEY"));
		 */

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        WeatherResponse result = client.fetch(city);

        assertEquals(23.4, result.getTemperature_degrees(), 0.001);
        assertEquals(11.1, result.getWind_speed(), 0.001);
    }

    @Test
    void shouldThrowExceptionOnIncompleteResponse() {
        String city = "melbourne";
        Map<String, Object> incomplete = new HashMap<>(); // No "main" or "wind"

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(incomplete);

        assertThrows(IllegalStateException.class, () -> client.fetch(city));
    }

    @Test
    void shouldThrowExceptionOnRestTemplateFailure() {
        String city = "melbourne";

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("API down"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> client.fetch(city));
        assertEquals("API down", exception.getMessage());
    }
}
