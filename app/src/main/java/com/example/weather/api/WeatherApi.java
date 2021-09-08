package com.example.weather.api;

import com.example.weather.api.model.WeatherDTO;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("data/2.5/onecall?exclude=minutely,alerts&units=metric&lang=ru")
    Single<WeatherDTO> getWeatherByCoordinate(@Query("lat") double lat, @Query("lon") double lon);

}
