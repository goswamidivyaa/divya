package com.example.demo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeatherResponseTest {

    @Test
    void testConstructorAndGetters() {
        double temp = 28.5;
        double wind = 13.2;

        WeatherResponse response = new WeatherResponse(temp, wind);

        assertEquals(temp, response.getTemperature_degrees(), 0.001);
        assertEquals(wind, response.getWind_speed(), 0.001);
    }
}
