package com.example.weather;

import android.app.Application;

import androidx.room.Room;

import com.example.weather.api.WeatherService;
import com.example.weather.data.AppDatabase;
import com.example.weather.data.CurrentDao;
import com.example.weather.data.DailyDao;
import com.example.weather.data.HourlyDao;

public class App extends Application {

    private WeatherService weatherService;

    private AppDatabase database;
    private CurrentDao currentDao;
    private HourlyDao hourlyDao;
    private DailyDao dailyDao;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "forecast-db")
                .build();

        currentDao = database.currentDao();
        hourlyDao = database.hourlyDao();
        dailyDao = database.dailyDao();

        weatherService = new WeatherService();
    }

    public CurrentDao getCurrentDao() {
        return currentDao;
    }

    public HourlyDao getHourlyDao() {
        return hourlyDao;
    }

    public DailyDao getDailyDao() {
        return dailyDao;
    }

    public WeatherService getWeatherService() {
        return weatherService;
    }

    public void clearAll() {
        database.clearAllTables();
    }
}
