package com.example.demo.util;

public class Constants {

	 // API Keys (env variable names)
    public static final String WEATHERSTACK_API_KEY_ENV = "WEATHERSTACK_API_KEY";
    public static final String OPENWEATHER_API_KEY_ENV = "OPENWEATHER_API_KEY";

    // API URL templates
    public static final String WEATHERSTACK_URL_TEMPLATE =
            "http://api.weatherstack.com/current?access_key=%s&query=%s";

    public static final String OPENWEATHER_URL_TEMPLATE =
            "http://api.openweathermap.org/data/2.5/weather?q=%s,AU&appid=%s&units=metric";

    // Default values
    public static final String DEFAULT_CITY = "melbourne";
    public static final int CACHE_DURATION_SECONDS = 3;
    
}