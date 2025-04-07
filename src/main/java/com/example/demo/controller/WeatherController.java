package com.example.demo.controller;

import com.example.demo.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.service.WeatherService;
import org.springframework.web.bind.annotation.*;
import static com.example.demo.util.Constants.DEFAULT_CITY;

@RestController
@RequestMapping("/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public WeatherResponse getWeather(@RequestParam(defaultValue = DEFAULT_CITY) String city) {
    	 logger.info("Received request for weather in '{}'", city);
    	 WeatherResponse response = weatherService.getWeather(city.toLowerCase());
         logger.info("Responding with temperature={}°C and windSpeed={} km/h for '{}'",
                 response.getTemperature_degrees(), response.getWind_speed(), city.toLowerCase());
         return response;
    }
}
