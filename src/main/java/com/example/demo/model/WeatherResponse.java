package com.example.demo.model;

public class WeatherResponse {
    private double temperature_degrees;
    private double wind_speed;

    public WeatherResponse(double temperature_degrees, double wind_speed) {
        this.temperature_degrees = temperature_degrees;
        this.wind_speed = wind_speed;
    }

    public double getTemperature_degrees() { return temperature_degrees; }
    public double getWind_speed() { return wind_speed; }
}
