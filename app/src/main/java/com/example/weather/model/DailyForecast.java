package com.example.weather.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyForecast {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "dt")
    private int dt;

    @ColumnInfo(name = "icon")
    private String icon;

    @ColumnInfo(name = "dayTemp")
    private double dayTemp;

    @ColumnInfo(name = "nightTemp")
    private double nightTemp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getDayTemp() {
        return dayTemp;
    }

    public void setDayTemp(double dayTemp) {
        this.dayTemp = dayTemp;
    }

    public double getNightTemp() {
        return nightTemp;
    }

    public void setNightTemp(double nightTemp) {
        this.nightTemp = nightTemp;
    }
}
