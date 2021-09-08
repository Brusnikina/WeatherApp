package com.example.weather.screens.recyclerAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.HourlyForecast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder> {

    ArrayList<HourlyForecast> hours = new ArrayList<HourlyForecast>();

    public void setHourlyForecast(List<HourlyForecast> hourlyForecasts) {
        hours.clear();
        hours.addAll(hourlyForecasts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HourlyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        holder.bind(hours.get(position));
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }

    static class HourlyViewHolder extends RecyclerView.ViewHolder {

        HourlyForecast weather;

        TextView hourlyTemp;
        TextView hourlyTime;
        ImageView hourlyWeather;

        public HourlyViewHolder(@NonNull View itemView) {
            super(itemView);

            hourlyTemp = itemView.findViewById(R.id.tv_hourly_temp);
            hourlyTime = itemView.findViewById(R.id.tv_hourly_time);
            hourlyWeather = itemView.findViewById(R.id.iv_hourly_weather);
        }

        public void bind(HourlyForecast hourlyForecast) {
            weather = hourlyForecast;

            Locale locale = itemView.getResources().getConfiguration().locale;
            SimpleDateFormat simpleDate = new SimpleDateFormat("H:mm", locale);
            hourlyTime.setText(simpleDate.format((long) weather.getDt() * 1000));

            String temp = String.format(itemView.getContext().getString(R.string.tempriture_description), weather.getTemp());
            hourlyTemp.setText(temp);

            String name = itemView.getContext().getString(R.string.icon_frefix) + weather.getIcon();
            int id = itemView.getResources().getIdentifier(name,
                    itemView.getContext().getString(R.string.drawable_type), itemView.getContext().getPackageName());
            hourlyWeather.setImageResource(id);
        }
    }
}
