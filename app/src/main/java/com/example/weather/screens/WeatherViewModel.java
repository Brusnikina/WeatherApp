package com.example.weather.screens;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather.App;
import com.example.weather.model.CurrentForecast;
import com.example.weather.model.DailyForecast;
import com.example.weather.model.HourlyForecast;

import java.util.List;

public class WeatherViewModel extends ViewModel {

    private LiveData<List<CurrentForecast>> currentForecasts = App.getInstance().getCurrentDao().getCurrentForecasts();
    private LiveData<List<HourlyForecast>> hourlyForecasts = App.getInstance().getHourlyDao().getHourlyForecasts();
    private LiveData<List<DailyForecast>> dailyForecasts = App.getInstance().getDailyDao().getDailyForecasts();

    public LiveData<List<CurrentForecast>> getCurrentForecasts() {
        return currentForecasts;
    }

    public LiveData<List<HourlyForecast>> getHourlyForecasts() {
        return hourlyForecasts;
    }

    public LiveData<List<DailyForecast>> getDailyForecasts() {
        return dailyForecasts;
    }
}
