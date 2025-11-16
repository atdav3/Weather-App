package com.example.app.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.app.R;
import com.example.app.services.HistoryService;
import com.example.app.database.UserDatabaseHelper;

import java.io.File;

public class PerformanceFragment extends Fragment {
    private TextView tvDbSize;
    private TextView tvTablesStats;
    private Button btnClearHistories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_performance, container, false);
        tvDbSize = root.findViewById(R.id.tvDbSize);
        tvTablesStats = root.findViewById(R.id.tvTablesStats);
        btnClearHistories = root.findViewById(R.id.btnClearHistories);

        renderStats();

        btnClearHistories.setOnClickListener(v -> {
            HistoryService hs = new HistoryService(requireContext());
            hs.clearSearchHistory();
            hs.clearWeatherViewHistory();
            renderStats();
        });

        return root;
    }

    private void renderStats() {
        if (getContext() == null) return;
        File db1 = requireContext().getDatabasePath("weather_app.db");
        File db2 = requireContext().getDatabasePath("auth.db");
        long size = (db1 != null && db1.exists() ? db1.length() : 0)
                + (db2 != null && db2.exists() ? db2.length() : 0);
        tvDbSize.setText("Kích thước DB: " + formatSize(size));

        HistoryService hs = new HistoryService(requireContext());
        int searches = hs.getSearchHistoryCount();
        int favorites = hs.getFavoriteLocationsCount();
        int views = 0;
        if (hs.getRecentWeatherViews(100) != null) views = hs.getRecentWeatherViews(100).size();

        UserDatabaseHelper udb = new UserDatabaseHelper(requireContext());
        int users = udb.countUsers();

        tvTablesStats.setText("Bản ghi - users:" + users + ", searches:" + searches + ", favorites:" + favorites + ", views:" + views);
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = ("KMGTPE").charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}


