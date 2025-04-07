# Zai Weather Service

	A lightweight and resilient HTTP service that reports Melbourne's weather, built in Java using Spring Boot. 
	This service fetches real-time weather data from WeatherStack (primary) and falls back to OpenWeatherMap (secondary) when needed.

# Features

	- ✅ Returns temperature (°C) and wind speed via a clean JSON API
	- 🔄 Fallback mechanism between WeatherStack and OpenWeatherMap
	- ⚡ In-memory caching (3 seconds) with stale response fallback
	- 💡 Designed for scalability, readability, and easy maintenance
	- 🧱 Extensible: new providers can be added easily
	
	
# Tech Stack
	
	- Java 17+
	- Spring Boot
	- RestTemplate (HTTP client)
	- Maven
	- Jackson (for JSON parsing)
	
# How to Run

    1. Clone the repository

    - using git bash
	- https://github.com/goswamidivyaa/divya.git
	- cd divya
	
    2. Add your API keys
      - Environment Variables in Eclipse :

			1. Define Env Variables in Eclipse
				- Right-click your project in Eclipse → choose Run As → Run Configurations.
				- In the left panel, select your application under Java Application.
				- Go to the Environment tab.
				- Click New... and add:
		
				     Name	                          Value
				WEATHERSTACK_API_KEY	    your_weatherstack_key
				OPENWEATHERMAP_API_KEY	    your_openweathermap_key
				
		
    3.  Build and run the project.
    
   
    4.  The server will start at http://localhost:9000.
    
   
    5.  Example Request
   
         http://localhost:/v1/weather?city=melbourne
      
    6. Sample Response

		{
		  "temperatureDegrees": 21.0,
		  "windSpeed": 13.2
		}
		
Note: The city query param is optional and defaults to "melbourne".
   		