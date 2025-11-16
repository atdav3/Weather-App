package com.example.app.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.app.R;
import com.example.app.database.UserDatabaseHelper;
import com.example.app.services.HistoryService;

public class OverviewFragment extends Fragment {
    private TextView tvUsersCount;
    private TextView tvSearchesCount;
    private TextView tvFavoritesCount;
    private TextView tvViewsCount;
    private BarChart barChartSearches;
    private RecyclerView rvAdminStats;
    private OverviewStatsAdapter statsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_overview, container, false);
        tvUsersCount = root.findViewById(R.id.tvUsersCount);
        tvSearchesCount = root.findViewById(R.id.tvSearchesCount);
        tvFavoritesCount = root.findViewById(R.id.tvFavoritesCount);
        tvViewsCount = root.findViewById(R.id.tvViewsCount);
        barChartSearches = root.findViewById(R.id.barChartSearches);
        rvAdminStats = root.findViewById(R.id.rvAdminStats);

        rvAdminStats.setLayoutManager(new LinearLayoutManager(requireContext()));
        statsAdapter = new OverviewStatsAdapter(requireContext(), new java.util.ArrayList<>());
        rvAdminStats.setAdapter(statsAdapter);

        loadOverviewData();
        return root;
    }

    private void loadOverviewData() {
        if (getContext() == null) return;
        // Users count via auth database
        UserDatabaseHelper userDb = new UserDatabaseHelper(getContext());
        int users = userDb.countUsers();
        tvUsersCount.setText("Người dùng: " + users);

        // History and favorites via HistoryService
        HistoryService historyService = new HistoryService(getContext());
        int searches = historyService.getSearchHistoryCount();
        int favorites = historyService.getFavoriteLocationsCount();
        int recentViews = 0;
        try {
            if (historyService.getRecentWeatherViews(50) != null) {
                recentViews = historyService.getRecentWeatherViews(50).size();
            }
        } catch (Exception ignored) {}

        tvSearchesCount.setText("Lịch sử tìm kiếm: " + searches);
        tvFavoritesCount.setText("Địa điểm yêu thích: " + favorites);
        tvViewsCount.setText("Lượt xem thời tiết gần đây: " + recentViews);

        // Simple sample bar chart: last 7 days searches (mock from counts)
        try {
            int base = Math.max(1, searches / 7);
            java.util.List<BarEntry> entries = new java.util.ArrayList<>();
            for (int i = 0; i < 7; i++) {
                entries.add(new BarEntry(i, base + (i * 2) % (base + 5)));
            }
            BarDataSet dataSet = new BarDataSet(entries, "Lượt tìm 7 ngày");
            dataSet.setColor(android.graphics.Color.parseColor("#29B6F6"));
            BarData data = new BarData(dataSet);
            data.setBarWidth(0.9f);
            barChartSearches.setData(data);
            barChartSearches.setFitBars(true);
            barChartSearches.getDescription().setEnabled(false);
            XAxis xAxis = barChartSearches.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            barChartSearches.getAxisRight().setEnabled(false);
            barChartSearches.invalidate();
        } catch (Exception ignored) {}

        // Fill top locations progress (mock from favorites/searches)
        try {
            java.util.List<OverviewStatsAdapter.StatItem> items = new java.util.ArrayList<>();
            int total = Math.max(1, searches);
            // Mock 5 rows with descending percentage
            int[] percents = new int[]{85, 75, 60, 50, 35};
            String[] labels = new String[]{"Hà Nội", "TP.HCM", "Đà Nẵng", "Cần Thơ", "Nha Trang"};
            for (int i = 0; i < labels.length; i++) {
                items.add(new OverviewStatsAdapter.StatItem(labels[i], percents[i]));
            }
            statsAdapter.update(items);
        } catch (Exception ignored) {}
    }
}


