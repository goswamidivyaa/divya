package com.example.demo.service;

import com.example.demo.model.WeatherResponse;
import com.example.demo.provider.OpenWeatherClient;
import com.example.demo.provider.WeatherStackClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceTest {

    private WeatherStackClient weatherStackClient;
    private OpenWeatherClient openWeatherClient;
    private WeatherService weatherService;

    private final String city = "melbourne";

    @BeforeEach
    void setUp() {
        weatherStackClient = mock(WeatherStackClient.class);
        openWeatherClient = mock(OpenWeatherClient.class);
        weatherService = new WeatherService(weatherStackClient, openWeatherClient);
    }

    @Test
    void shouldReturnCachedWeatherIfPresent() {
        WeatherResponse response = new WeatherResponse(22.0, 10.0);
        // Inject cache manually before calling getWeather
        try {
            var cacheField = WeatherService.class.getDeclaredField("cache");
            cacheField.setAccessible(true);
            @SuppressWarnings("unchecked")
            var cache = (com.github.benmanes.caffeine.cache.Cache<String, WeatherResponse>) cacheField.get(weatherService);
            cache.put(city, response);
        } catch (Exception e) {
            fail("Failed to inject cache manually");
        }
        WeatherResponse cached = weatherService.getWeather(city);
        assertNotNull(cached, "Cached response should not be null");
        assertEquals(22.0, cached.getTemperature_degrees());
        assertEquals(10.0, cached.getWind_speed());
        verifyNoInteractions(weatherStackClient);
        verifyNoInteractions(openWeatherClient);
    }


    @Test
    void shouldReturnWeatherFromWeatherStack() {
        WeatherResponse wsResponse = new WeatherResponse(25.0, 12.0);
        when(weatherStackClient.fetch(city)).thenReturn(wsResponse);
        WeatherResponse result = weatherService.getWeather(city);
        assertEquals(25.0, result.getTemperature_degrees());
        assertEquals(12.0, result.getWind_speed());
        verify(weatherStackClient, times(1)).fetch(city);
        verifyNoInteractions(openWeatherClient);
    }

    @Test
    void shouldFallbackToOpenWeatherIfWeatherStackFails() {
        WeatherResponse owResponse = new WeatherResponse(26.5, 14.0);
        when(weatherStackClient.fetch(city)).thenThrow(new RuntimeException("WeatherStack down"));
        when(openWeatherClient.fetch(city)).thenReturn(owResponse);
        WeatherResponse result = weatherService.getWeather(city);
        assertEquals(26.5, result.getTemperature_degrees());
        assertEquals(14.0, result.getWind_speed());
        verify(weatherStackClient, times(1)).fetch(city);
        verify(openWeatherClient, times(1)).fetch(city);
    }

    @Test
    void shouldReturnStaleCacheIfBothProvidersFail() {
        WeatherResponse cachedResponse = new WeatherResponse(20.0, 9.0);
        // Manually cache data
        try {
            var cacheField = WeatherService.class.getDeclaredField("cache");
            cacheField.setAccessible(true);
            var cache = (com.github.benmanes.caffeine.cache.Cache<String, WeatherResponse>) cacheField.get(weatherService);
            cache.put(city, cachedResponse);
        } catch (Exception e) {
            fail("Failed to inject cache manually");
        }
        when(weatherStackClient.fetch(city)).thenThrow(new RuntimeException("WeatherStack down"));
        when(openWeatherClient.fetch(city)).thenThrow(new RuntimeException("OpenWeather down"));
        WeatherResponse result = weatherService.getWeather(city);
        assertEquals(20.0, result.getTemperature_degrees());
        assertEquals(9.0, result.getWind_speed());
    }

    @Test
    void shouldReturnNullIfAllFailsAndNoCache() {
        when(weatherStackClient.fetch(city)).thenThrow(new RuntimeException("WeatherStack down"));
        when(openWeatherClient.fetch(city)).thenThrow(new RuntimeException("OpenWeather down"));
        WeatherResponse result = weatherService.getWeather(city);
        assertNull(result);
    }
}
