package com.example.weather.screens.recyclerAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.DailyForecast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {

    ArrayList<DailyForecast> days = new ArrayList<>();

    public void setDailyForecast(List<DailyForecast> days) {
        this.days.clear();
        this.days.addAll(days);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyAdapter.DailyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        holder.bind(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class DailyViewHolder extends RecyclerView.ViewHolder {

        DailyForecast weather;

        TextView dailyDate;
        TextView dailyDayTemp;
        TextView dailyNightTemp;
        ImageView dailyWeather;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);

            dailyDate = itemView.findViewById(R.id.tv_daily_date);
            dailyDayTemp = itemView.findViewById(R.id.tv_daily_day_temp);
            dailyNightTemp = itemView.findViewById(R.id.tv_daily_night_temp);
            dailyWeather = itemView.findViewById(R.id.iv_daily_weather);
        }

        public void bind(DailyForecast dailyForecast) {
            weather = dailyForecast;

            Locale locale = itemView.getResources().getConfiguration().locale;
            SimpleDateFormat simpleDate = new SimpleDateFormat("EEE, d MMMM", locale);

            String date = simpleDate.format((long) weather.getDt() * 1000);
            StringBuilder sb = new StringBuilder(date);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            dailyDate.setText(sb);

            String tempDay = String.format(itemView.getContext().getString(R.string.tempriture_description), weather.getDayTemp());
            String tempNight = String.format(itemView.getContext().getString(R.string.tempriture_description), weather.getNightTemp());
            dailyDayTemp.setText(tempDay);
            dailyNightTemp.setText(tempNight);

            String name = itemView.getContext().getString(R.string.icon_frefix) + weather.getIcon();
            int id = itemView.getResources().getIdentifier(name,
                    itemView.getContext().getString(R.string.drawable_type), itemView.getContext().getPackageName());
            dailyWeather.setImageResource(id);
        }
    }
}
