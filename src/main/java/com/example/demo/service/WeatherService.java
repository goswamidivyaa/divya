package com.example.demo.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.example.demo.model.WeatherResponse;
import com.example.demo.provider.OpenWeatherClient;
import com.example.demo.provider.WeatherStackClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class WeatherService {

    private final WeatherStackClient weatherStackClient;
    private final OpenWeatherClient openWeatherClient;
    private final Cache<String, WeatherResponse> cache;

    public WeatherService(WeatherStackClient weatherStackClient, OpenWeatherClient openWeatherClient) {
        this.weatherStackClient = weatherStackClient;
        this.openWeatherClient = openWeatherClient;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build();
    }

    public WeatherResponse getWeather(String city) {
        WeatherResponse cached = cache.getIfPresent(city);
        if (cached != null) return cached;

        try {
            WeatherResponse wsData = weatherStackClient.fetch(city);
            cache.put(city, wsData);
            return wsData;
        } catch (Exception ignored) {
            try {
                WeatherResponse owData = openWeatherClient.fetch(city);
                cache.put(city, owData);
                return owData;
            } catch (Exception ignoredAgain) {
                return cached; // stale
            }
        }
    }
}
