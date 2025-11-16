package com.example.app.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;

import java.util.List;

public class OverviewStatsAdapter extends RecyclerView.Adapter<OverviewStatsAdapter.VH> {
    public static class StatItem {
        public final String label;
        public final int percent;
        public StatItem(String label, int percent) {
            this.label = label;
            this.percent = percent;
        }
    }

    private final Context context;
    private List<StatItem> items;

    public OverviewStatsAdapter(Context context, List<StatItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_admin_stat, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        StatItem item = items.get(position);
        holder.label.setText(item.label);
        holder.percent.setText(item.percent + "%");
        holder.progress.setProgress(item.percent);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void update(List<StatItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView label, percent; ProgressBar progress;
        VH(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.tvLabel);
            percent = itemView.findViewById(R.id.tvPercent);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}


