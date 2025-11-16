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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.DailyViewHolder> {

    private final Context context;
    private List<ForecastDay> dailyForecast;

    public DailyForecastAdapter(Context context, List<ForecastDay> dailyForecast) {
        this.context = context;
        this.dailyForecast = dailyForecast;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_daily_forecast, parent, false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        ForecastDay day = dailyForecast.get(position);

        holder.tvDayName.setText(getVietnameseDayLabel(day.getDate()));

        int iconResource = WeatherUtils.getWeatherIcon(
                day.getDay().getCondition().getCode(), true);
        holder.ivDailyIcon.setImageResource(iconResource);

        holder.tvDailyDescription.setText(day.getDay().getCondition().getText());
        holder.tvDailyLowTemp.setText(WeatherUtils.formatTemperature(day.getDay().getMintempC()));
        holder.tvDailyHighTemp.setText(WeatherUtils.formatTemperature(day.getDay().getMaxtempC()));
    }

    @Override
    public int getItemCount() {
        return dailyForecast != null ? dailyForecast.size() : 0;
    }

    public void updateData(List<ForecastDay> newDailyForecast) {
        this.dailyForecast = newDailyForecast;
        notifyDataSetChanged();
        
        // Debug log để kiểm tra số lượng items
        android.util.Log.d("DailyForecastAdapter", "Updated with " + (newDailyForecast != null ? newDailyForecast.size() : 0) + " items");
        
        // Log chi tiết từng ngày
        if (newDailyForecast != null) {
            for (int i = 0; i < newDailyForecast.size(); i++) {
                ForecastDay day = newDailyForecast.get(i);
                if (day != null) {
                    android.util.Log.d("DailyForecastAdapter", "Day " + i + ": " + day.getDate() + " - " + 
                        (day.getDay() != null && day.getDay().getCondition() != null ? day.getDay().getCondition().getText() : "No data"));
                }
            }
        }
    }

    private String getVietnameseDayLabel(String dateStr) {
        // dateStr expected format: yyyy-MM-dd
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = df.parse(dateStr);
            if (date == null) return dateStr;

            Calendar c = Calendar.getInstance();
            // Today
            Calendar today = (Calendar) c.clone();
            resetTime(today);
            // Tomorrow
            Calendar tomorrow = (Calendar) today.clone();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);

            Calendar target = Calendar.getInstance();
            target.setTime(date);
            resetTime(target);

            if (target.equals(today)) return "Hôm nay";
            if (target.equals(tomorrow)) return "Ngày mai";

            // Weekday in Vietnamese
            SimpleDateFormat weekday = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
            String w = weekday.format(date);
            // Capitalize first letter consistently
            return w.substring(0, 1).toUpperCase(new Locale("vi", "VN")) + w.substring(1);
        } catch (ParseException e) {
            return dateStr;
        }
    }

    private void resetTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    static class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayName;
        ImageView ivDailyIcon;
        TextView tvDailyDescription;
        TextView tvDailyLowTemp;
        TextView tvDailyHighTemp;

        DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.tvDayName);
            ivDailyIcon = itemView.findViewById(R.id.ivDailyIcon);
            tvDailyDescription = itemView.findViewById(R.id.tvDailyDescription);
            tvDailyLowTemp = itemView.findViewById(R.id.tvDailyLowTemp);
            tvDailyHighTemp = itemView.findViewById(R.id.tvDailyHighTemp);
        }
    }
}


