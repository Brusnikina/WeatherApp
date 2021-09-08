package com.example.weather.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HourlyForecast {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "dt")
    private Integer dt;

    @ColumnInfo(name = "icon")
    private String icon;

    @ColumnInfo(name = "temp")
    private Double temp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}
