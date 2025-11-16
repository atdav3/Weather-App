package com.example.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.models.WeatherEvent;

import java.util.List;

public class WeatherEventAdapter extends RecyclerView.Adapter<WeatherEventAdapter.ViewHolder> {

    private final Context context;
    private List<WeatherEvent> weatherEvents;

    public WeatherEventAdapter(Context context, List<WeatherEvent> weatherEvents) {
        this.context = context;
        this.weatherEvents = weatherEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherEvent event = weatherEvents.get(position);
        holder.tvEventTitle.setText(event.getTitle());
        holder.tvEventDescription.setText(event.getDescription());
        holder.tvEventTime.setText(event.getTime());
    }

    @Override
    public int getItemCount() { return weatherEvents != null ? weatherEvents.size() : 0; }

    public void updateData(List<WeatherEvent> newEvents) { this.weatherEvents = newEvents; notifyDataSetChanged(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventTitle, tvEventDescription, tvEventTime;
        ViewHolder(View itemView) {
            super(itemView);
            tvEventTitle = itemView.findViewById(R.id.tvEventTitle);
            tvEventDescription = itemView.findViewById(R.id.tvEventDescription);
            tvEventTime = itemView.findViewById(R.id.tvEventTime);
        }
    }
}


