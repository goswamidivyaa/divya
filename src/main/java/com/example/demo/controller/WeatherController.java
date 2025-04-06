package com.example.demo.controller;

import com.example.demo.model.WeatherResponse;
import com.example.demo.service.WeatherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public WeatherResponse getWeather(@RequestParam(defaultValue = "melbourne") String city) {
        return weatherService.getWeather(city.toLowerCase());
    }
}
