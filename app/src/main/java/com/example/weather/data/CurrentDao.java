package com.example.weather.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weather.model.CurrentForecast;

import java.util.List;

@Dao
public interface CurrentDao {

    @Query("SELECT * FROM CurrentForecast")
    LiveData<List<CurrentForecast>> getCurrentForecasts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CurrentForecast currentForecast);

    @Update
    void update(CurrentForecast currentForecast);

    @Delete
    void delete(CurrentForecast currentForecast);
}
