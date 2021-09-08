package com.example.weather.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrentForecast {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "temp")
    private double temp;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "feelsLike")
    private double feelsLike;

    @ColumnInfo(name = "icon")
    private String icon;

    @ColumnInfo(name = "windSpeed")
    private double windSpeed;

    @ColumnInfo(name = "pressure")
    private int pressure;

    @ColumnInfo(name = "humidity")
    private int humidity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
