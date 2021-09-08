package com.example.weather.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weather.model.HourlyForecast;

import java.util.List;

@Dao
public interface HourlyDao {
    @Query("SELECT * FROM HourlyForecast ORDER BY dt")
    LiveData<List<HourlyForecast>> getHourlyForecasts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HourlyForecast hourlyForecast);

    @Update
    void update(HourlyForecast hourlyForecast);

    @Delete
    void delete(HourlyForecast hourlyForecast);
}
