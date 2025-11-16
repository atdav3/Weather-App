package com.example.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.models.ForecastDay;
import com.example.app.utils.WeatherUtils;

import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyViewHolder> {

    private final Context context;
    private List<ForecastDay.Hour> hourlyForecast;

    public HourlyForecastAdapter(Context context, List<ForecastDay.Hour> hourlyForecast) {
        this.context = context;
        this.hourlyForecast = hourlyForecast;
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hourly_forecast, parent, false);
        return new HourlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        ForecastDay.Hour hour = hourlyForecast.get(position);

        holder.tvHourlyTime.setText(WeatherUtils.formatHourlyTime(hour.getTime()));
        int iconResource = WeatherUtils.getWeatherIcon(hour.getCondition().getCode(), hour.getIsDay() == 1);
        holder.ivHourlyIcon.setImageResource(iconResource);
        // Ensure no unintended tint so the vector/icon keeps its intended color
        holder.ivHourlyIcon.setImageTintList(null);
        holder.tvHourlyTemp.setText(WeatherUtils.formatTemperature(hour.getTempC()));
    }

    @Override
    public int getItemCount() {
        return hourlyForecast != null ? hourlyForecast.size() : 0;
    }

    public void updateData(List<ForecastDay.Hour> newHourlyForecast) {
        this.hourlyForecast = newHourlyForecast;
        notifyDataSetChanged();
    }

    static class HourlyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHourlyTime;
        ImageView ivHourlyIcon;
        TextView tvHourlyTemp;

        HourlyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHourlyTime = itemView.findViewById(R.id.tvHourlyTime);
            ivHourlyIcon = itemView.findViewById(R.id.ivHourlyIcon);
            tvHourlyTemp = itemView.findViewById(R.id.tvHourlyTemp);
        }
    }
}


