package com.example.weather.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.weather.model.CurrentForecast;
import com.example.weather.model.DailyForecast;
import com.example.weather.model.HourlyForecast;

@Database(entities = {CurrentForecast.class, HourlyForecast.class, DailyForecast.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CurrentDao currentDao();
    public abstract HourlyDao hourlyDao();
    public abstract DailyDao dailyDao();
}
