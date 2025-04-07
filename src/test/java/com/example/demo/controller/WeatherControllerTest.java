package com.example.demo.controller;

import com.example.demo.model.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private com.example.demo.service.WeatherService weatherService;

    @Test
    void testGetWeatherIntegration() {
        when(weatherService.getWeather("melbourne")).thenReturn(new WeatherResponse(20.5, 9.0));

        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity("/v1/weather", WeatherResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTemperature_degrees()).isEqualTo(20.5);
        assertThat(response.getBody().getWind_speed()).isEqualTo(9.0);
    }
}