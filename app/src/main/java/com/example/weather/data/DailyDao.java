package com.example.weather.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weather.model.DailyForecast;

import java.util.List;

@Dao
public interface DailyDao {
    @Query("SELECT * FROM DailyForecast ORDER BY dt")
    LiveData<List<DailyForecast>> getDailyForecasts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DailyForecast dailyForecast);

    @Update
    void update(DailyForecast dailyForecast);

    @Delete
    void delete(DailyForecast dailyForecast);
}
