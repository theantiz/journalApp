package com.antiz.journalApp.service;

import com.antiz.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private static final String apiKey = "e32b8fc18065adb4523b3de4c788c079";
    private static final String API = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        String key = "weather_of_" + city;
        WeatherResponse weatherResponse = redisService.get(key, WeatherResponse.class);
        WeatherResponse body;
        if (weatherResponse != null) {
            return weatherResponse;
        } else {
            String finalAPI = API.replace("city", city).replace("API_KEY", apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            body = response.getBody();

            if(body != null){
                redisService.set("wheather of " + city, body, 300l);
            }
            return body;

        }

    }


     /*
    // Example POST call using RestTemplate (commented out)
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // If the API expects a body, construct it here (example: a Map or POJO)
    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("city", city);
    requestBody.put("apiKey", apiKey);

    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<WeatherResponse> postResponse = restTemplate.exchange(finalAPI, HttpMethod.POST, requestEntity, WeatherResponse.class);
    WeatherResponse postBody = postResponse.getBody();
    // return postBody; // Uncomment if you want to use POST instead of GET
    */
}
