package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app.R;

import com.example.app.adapters.SearchHistoryAdapter;
import com.example.app.models.SearchHistory;
import com.example.app.services.HistoryService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private FloatingActionButton fabClear;
    
    private HistoryService historyService;
    private SearchHistoryAdapter adapter;
    private List<SearchHistory> allHistoryList;
    private List<SearchHistory> favoriteList;
    
    private static final int TAB_HISTORY = 0;
    private static final int TAB_FAVORITES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        initViews();
        initToolbar();
        initTabLayout();
        initRecyclerView();
        initHistoryService();
        loadData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        recyclerView = findViewById(R.id.recycler_view);
        emptyStateText = findViewById(R.id.tv_empty_state);
        fabClear = findViewById(R.id.fab_clear);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lịch sử & Yêu thích");
        }
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Lịch sử"));
        tabLayout.addTab(tabLayout.newTab().setText("Yêu thích"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_HISTORY:
                        showHistoryTab();
                        break;
                    case TAB_FAVORITES:
                        showFavoritesTab();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void initRecyclerView() {
        adapter = new SearchHistoryAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        // Thiết lập các listener
        adapter.setOnItemClickListener(searchHistory -> {
            // Chuyển về MainActivity với địa điểm được chọn
            Intent intent = new Intent();
            intent.putExtra("location_name", searchHistory.getLocationName());
            intent.putExtra("region", searchHistory.getRegion());
            intent.putExtra("country", searchHistory.getCountry());
            intent.putExtra("latitude", searchHistory.getLatitude());
            intent.putExtra("longitude", searchHistory.getLongitude());
            setResult(RESULT_OK, intent);
            finish();
        });
        
        adapter.setOnFavoriteClickListener((searchHistory, isFavorite) -> {
            searchHistory.setFavorite(isFavorite);
            if (historyService.updateSearchHistory(searchHistory)) {
                adapter.updateItem(searchHistory);
                updateTabBadges();
                Toast.makeText(this, 
                    isFavorite ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích", 
                    Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
        
        adapter.setOnDeleteClickListener(searchHistory -> {
            showDeleteConfirmDialog(searchHistory);
        });
    }

    private void initHistoryService() {
        historyService = new HistoryService(this);
        // Clear all FAB
        fabClear.setOnClickListener(v -> showClearAllConfirmDialog());
    }

    private void loadData() {
        // Load dữ liệu trong background
        new Thread(() -> {
            allHistoryList = historyService.getAllSearchHistory();
            favoriteList = historyService.getFavoriteLocations();
            
            runOnUiThread(() -> {
                showHistoryTab();
                updateTabBadges();
            });
        }).start();
    }

    private void showHistoryTab() {
        if (allHistoryList != null && !allHistoryList.isEmpty()) {
            adapter.updateData(allHistoryList);
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
            fabClear.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
            emptyStateText.setText("Chưa có lịch sử tìm kiếm");
            fabClear.setVisibility(View.GONE);
        }
    }

    private void showFavoritesTab() {
        if (favoriteList != null && !favoriteList.isEmpty()) {
            adapter.updateData(favoriteList);
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
            fabClear.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
            emptyStateText.setText("Chưa có địa điểm yêu thích");
            fabClear.setVisibility(View.GONE);
        }
    }

    private void updateTabBadges() {
        if (allHistoryList != null) {
            TabLayout.Tab historyTab = tabLayout.getTabAt(TAB_HISTORY);
            if (historyTab != null) {
                historyTab.setText("Lịch sử (" + allHistoryList.size() + ")");
            }
        }
        
        if (favoriteList != null) {
            TabLayout.Tab favoriteTab = tabLayout.getTabAt(TAB_FAVORITES);
            if (favoriteTab != null) {
                favoriteTab.setText("Yêu thích (" + favoriteList.size() + ")");
            }
        }
    }

    private void showDeleteConfirmDialog(SearchHistory searchHistory) {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa địa điểm này khỏi lịch sử?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                if (historyService.deleteSearchHistory(searchHistory.getId())) {
                    adapter.removeItem(searchHistory);
                    allHistoryList.remove(searchHistory);
                    if (searchHistory.isFavorite()) {
                        favoriteList.remove(searchHistory);
                    }
                    updateTabBadges();
                    updateEmptyState();
                    Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void updateEmptyState() {
        if (tabLayout.getSelectedTabPosition() == TAB_HISTORY) {
            showHistoryTab();
        } else {
            showFavoritesTab();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_clear_all) {
            showClearAllConfirmDialog();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void showClearAllConfirmDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Xóa tất cả")
            .setMessage("Bạn có chắc chắn muốn xóa tất cả lịch sử tìm kiếm?")
            .setPositiveButton("Xóa tất cả", (dialog, which) -> {
                if (historyService.clearSearchHistory()) {
                    allHistoryList.clear();
                    favoriteList.clear();
                    adapter.updateData(new ArrayList<>());
                    updateTabBadges();
                    updateEmptyState();
                    Toast.makeText(this, "Đã xóa tất cả lịch sử", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (historyService != null) {
            historyService.close();
        }
    }
}
