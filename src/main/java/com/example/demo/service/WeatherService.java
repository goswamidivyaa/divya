package com.example.demo.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.example.demo.model.WeatherResponse;
import com.example.demo.provider.OpenWeatherClient;
import com.example.demo.provider.WeatherStackClient;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import static com.example.demo.util.Constants.CACHE_DURATION_SECONDS;

@Service
public class WeatherService {

    private final WeatherStackClient weatherStackClient;
    private final OpenWeatherClient openWeatherClient;
    private final Cache<String, WeatherResponse> cache;
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    public WeatherService(WeatherStackClient weatherStackClient, OpenWeatherClient openWeatherClient) {
        this.weatherStackClient = weatherStackClient;
        this.openWeatherClient = openWeatherClient;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(CACHE_DURATION_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    @SuppressWarnings("unused")
	public WeatherResponse getWeather(String city) {
        WeatherResponse cached = cache.getIfPresent(city);
        if (cached != null) {
        	 logger.info("Returning cached weather for city '{}': temperature={}°C, windSpeed={} km/h",
                     city, cached.getTemperature_degrees(), cached.getWind_speed());
        	return cached;
        }

        try {
        	logger.info("Fetching weather from WeatherStack for city '{}'", city);
            WeatherResponse wsData = weatherStackClient.fetch(city);
            logger.debug("WeatherStack response for '{}': temperature={}°C, windSpeed={} km/h",
                    city, wsData.getTemperature_degrees(), wsData.getWind_speed());
            cache.put(city, wsData);
            return wsData;
            
        } catch (Exception ignored) {
        	
        	 logger.warn("WeatherStack failed for '{}': {}. Falling back to OpenWeatherMap",
                     city, ignored.getMessage());
            try {
            	logger.info("Fetching weather from OpenWeatherMap for city '{}'", city);
                WeatherResponse owData = openWeatherClient.fetch(city);
                logger.debug("OpenWeatherMap response for '{}': temperature={}°C, windSpeed={} km/h",
                        city, owData.getTemperature_degrees(), owData.getWind_speed());
                cache.put(city, owData);
                return owData;
            } catch (Exception ignoredAgain) {
            	 logger.error("Both providers failed for '{}'. Returning stale cache if available. Error: {}",
                         city, ignoredAgain.getMessage());
            	 
            	  if (cached != null) {
                      logger.warn("Returning stale cached weather for '{}'", city);
                  } 
            	  
            	  else {
                      logger.error("No cached data available for '{}'", city);
                  }
            	  
                  return cached;
            }
        }
    }
}
