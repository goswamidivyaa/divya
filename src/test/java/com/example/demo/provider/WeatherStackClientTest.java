package com.example.demo.provider;

import com.example.demo.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherStackClientTest {

    private RestTemplate restTemplate;
    private WeatherStackClient client;

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        client = new WeatherStackClient(restTemplate);
    }

    @Test
    void shouldReturnValidWeatherResponse() {
        String city = "melbourne";

        Map<String, Object> current = new HashMap<>();
        current.put("temperature", 21.0);
        current.put("wind_speed", 15.0);

        Map<String, Object> response = new HashMap<>();
        response.put("current", current);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        WeatherResponse result = client.fetch(city);

        assertEquals(21.0, result.getTemperature_degrees(), 0.001);
        assertEquals(15.0, result.getWind_speed(), 0.001);
    }

    @Test
    void shouldThrowExceptionOnIncompleteResponse() {
        String city = "melbourne";
        Map<String, Object> incomplete = new HashMap<>(); // No "current"

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(incomplete);

        assertThrows(IllegalStateException.class, () -> client.fetch(city));
    }

    @Test
    void shouldThrowExceptionOnRestTemplateFailure() {
        String city = "melbourne";

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> client.fetch(city));
        assertEquals("Service unavailable", ex.getMessage());
    }
}
